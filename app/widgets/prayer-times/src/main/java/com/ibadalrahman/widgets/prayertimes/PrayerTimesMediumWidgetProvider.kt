package com.ibadalrahman.widgets.prayertimes

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import java.util.Locale
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PrayerTimesMediumWidgetProvider: AppWidgetProvider() {
    companion object {
        private const val TAG = "PrayerTimesMediumWidget"
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun viewModel(): PrayerTimesMediumWidgetViewModel
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // Handle locale change broadcasts
        when (intent.action) {
            Intent.ACTION_LOCALE_CHANGED -> {
                Log.d(TAG, "Locale changed, updating all widgets")
                // Update all widgets when locale changes
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val thisWidget = android.content.ComponentName(context, PrayerTimesMediumWidgetProvider::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

                if (appWidgetIds.isNotEmpty()) {
                    onUpdate(context, appWidgetManager, appWidgetIds)
                }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate called with ${appWidgetIds.size} widgets")

        // Schedule periodic updates
        PrayerTimesMediumWidgetUpdateWorker.scheduleUpdates(context)

        appWidgetIds.forEach { appWidgetId ->
            coroutineScope.launch {
                try {
                    Log.d(TAG, "Updating widget $appWidgetId")
                    updateWidget(context, appWidgetManager, appWidgetId)
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating widget $appWidgetId", e)
                    showErrorWidget(context, appWidgetManager, appWidgetId, e.message ?: "Unknown error")
                }
            }
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "onEnabled - First widget added")
        PrayerTimesMediumWidgetUpdateWorker.scheduleUpdates(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled - Last widget removed")
        job.cancel()
        PrayerTimesMediumWidgetUpdateWorker.cancelUpdates(context)

        // Cancel any scheduled alarms
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, PrayerTimesMediumWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private suspend fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.prayer_times_medium_widget_layout)

        // Apply RTL layout direction if current locale is RTL
        applyLayoutDirection(views)

        // Set up click intent to open the app
        val intent = Intent(context, Class.forName("org.ibadalrahman.publicsector.main.view.MainActivity"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        try {
            Log.d(TAG, "Getting ViewModel from EntryPoint")

            val entryPoint = EntryPoints.get(
                context.applicationContext,
                WidgetEntryPoint::class.java
            )
            val viewModel = entryPoint.viewModel()

            Log.d(TAG, "Fetching prayer times from ViewModel")

            // Get prayer times from ViewModel
            viewModel.getPrayerTimes().fold(
                onSuccess = { prayerData ->
                    Log.d(TAG, "Successfully fetched prayer times: ${prayerData.prayerTimes.size} prayers")

                    // Update dates
                    val dateParts = prayerData.date.split(" ")
                    if (dateParts.size >= 3) {
                        views.setTextViewText(R.id.gregorian_day, dateParts[0])
                        views.setTextViewText(R.id.gregorian_month, dateParts[1])
                        views.setTextViewText(R.id.gregorian_year, dateParts[2])
                    }

                    val hijriParts = prayerData.hijriDate.split(" ")
                    if (hijriParts.size >= 3) {
                        views.setTextViewText(R.id.hijri_day, hijriParts[0])
                        views.setTextViewText(R.id.hijri_month, hijriParts[1])
                        views.setTextViewText(R.id.hijri_year, hijriParts[2])
                    }

                    // Update next prayer info
                    prayerData.nextPrayer?.let { nextPrayer ->
                        val prayerName = getLocalizedPrayerName(context, nextPrayer.prayerName)
                        val afterText = context.getString(com.ibadalrahman.resources.R.string.prayer_after)

                        views.setTextViewText(R.id.next_prayer_label, prayerName)
                        views.setTextViewText(R.id.next_prayer_name, " $afterText")
                        views.setChronometerCountDown(R.id.next_prayer_time, true)
                        views.setChronometer(R.id.next_prayer_time, nextPrayer.chronometerBaseTime, null, true)

                        // Schedule an update when the prayer time arrives
                        val updateTime = System.currentTimeMillis() + (nextPrayer.chronometerBaseTime - android.os.SystemClock.elapsedRealtime())
                        scheduleWidgetUpdate(context, updateTime)
                    } ?: run {
                        views.setTextViewText(R.id.next_prayer_label, "")
                        views.setTextViewText(R.id.next_prayer_name, "")
                        views.setChronometer(R.id.next_prayer_time, 0, null, false)
                    }

                    // First, clear all backgrounds
                    views.setInt(R.id.prayer_row_1, "setBackgroundResource", 0)
                    views.setInt(R.id.prayer_row_2, "setBackgroundResource", 0)
                    views.setInt(R.id.prayer_row_3, "setBackgroundResource", 0)
                    views.setInt(R.id.prayer_row_4, "setBackgroundResource", 0)
                    views.setInt(R.id.prayer_row_5, "setBackgroundResource", 0)
                    views.setInt(R.id.prayer_row_6, "setBackgroundResource", 0)

                    // Update prayer names and times
                    val prayers = PrayerTimesMediumWidgetViewModel.Prayer.entries.toTypedArray()
                    prayers.forEachIndexed { index, prayer ->
                        val time = prayerData.prayerTimes[prayer] ?: ""

                        Log.d(TAG, "Prayer ${prayer.name}: $time")

                        // Apply highlight if this is the current prayer
                        val isCurrentPrayer = prayerData.currentPrayer == prayer
                        if (isCurrentPrayer) {
                            val rowId = when (index) {
                                0 -> R.id.prayer_row_1
                                1 -> R.id.prayer_row_2
                                2 -> R.id.prayer_row_3
                                3 -> R.id.prayer_row_4
                                4 -> R.id.prayer_row_5
                                5 -> R.id.prayer_row_6
                                else -> null
                            }
                            rowId?.let {
                                views.setInt(it, "setBackgroundResource", R.drawable.prayer_time_highlight_background)
                            }
                        }

                        when (index) {
                            0 -> {
                                views.setTextViewText(R.id.text1, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time1, time)
                                if (isCurrentPrayer) {
                                    views.setTextColor(R.id.text1, android.graphics.Color.WHITE)
                                    views.setTextColor(R.id.time1, android.graphics.Color.WHITE)
                                }
                            }
                            1 -> {
                                views.setTextViewText(R.id.text2, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time2, time)
                                if (isCurrentPrayer) {
                                    views.setTextColor(R.id.text2, android.graphics.Color.WHITE)
                                    views.setTextColor(R.id.time2, android.graphics.Color.WHITE)
                                }
                            }
                            2 -> {
                                views.setTextViewText(R.id.text3, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time3, time)
                                if (isCurrentPrayer) {
                                    views.setTextColor(R.id.text3, android.graphics.Color.WHITE)
                                    views.setTextColor(R.id.time3, android.graphics.Color.WHITE)
                                }
                            }
                            3 -> {
                                views.setTextViewText(R.id.text4, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time4, time)
                                if (isCurrentPrayer) {
                                    views.setTextColor(R.id.text4, android.graphics.Color.WHITE)
                                    views.setTextColor(R.id.time4, android.graphics.Color.WHITE)
                                }
                            }
                            4 -> {
                                views.setTextViewText(R.id.text5, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time5, time)
                                if (isCurrentPrayer) {
                                    views.setTextColor(R.id.text5, android.graphics.Color.WHITE)
                                    views.setTextColor(R.id.time5, android.graphics.Color.WHITE)
                                }
                            }
                            5 -> {
                                views.setTextViewText(R.id.text6, context.getString(prayer.stringResId))
                                views.setTextViewText(R.id.time6, time)
                                if (isCurrentPrayer) {
                                    views.setTextColor(R.id.text6, android.graphics.Color.WHITE)
                                    views.setTextColor(R.id.time6, android.graphics.Color.WHITE)
                                }
                            }
                        }
                    }
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to fetch prayer times", error)
                    // Show error state
                    views.setTextViewText(R.id.gregorian_day, "")
                    views.setTextViewText(R.id.gregorian_month, "")
                    views.setTextViewText(R.id.gregorian_year, "")
                    views.setTextViewText(R.id.hijri_day, "")
                    views.setTextViewText(R.id.hijri_month, "")
                    views.setTextViewText(R.id.hijri_year, "")
                    views.setTextViewText(R.id.next_prayer_label, "")
                    views.setTextViewText(R.id.next_prayer_name, "")
                    views.setChronometer(R.id.next_prayer_time, 0, null, false)
                    views.setTextViewText(R.id.text1, "Error loading")
                    views.setTextViewText(R.id.text2, "prayer times")
                    views.setTextViewText(R.id.text3, error.message ?: "")
                    views.setTextViewText(R.id.text4, "")
                    views.setTextViewText(R.id.text5, "")
                    views.setTextViewText(R.id.text6, "")
                    views.setTextViewText(R.id.time1, "")
                    views.setTextViewText(R.id.time2, "")
                    views.setTextViewText(R.id.time3, "")
                    views.setTextViewText(R.id.time4, "")
                    views.setTextViewText(R.id.time5, "")
                    views.setTextViewText(R.id.time6, "")
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Exception in updateWidget", e)
            throw e
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun applyLayoutDirection(views: RemoteViews) {
        val currentLocale = Locale.getDefault()
        val isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(currentLocale) == View.LAYOUT_DIRECTION_RTL

        Log.d(TAG, "Current locale: ${currentLocale.language}, isRtl: $isRtl")

        if (isRtl) {
            // Apply RTL layout direction to the root container
            views.setInt(R.id.widget_root, "setLayoutDirection", View.LAYOUT_DIRECTION_RTL)
        } else {
            // Ensure LTR layout direction for non-RTL locales
            views.setInt(R.id.widget_root, "setLayoutDirection", View.LAYOUT_DIRECTION_LTR)
        }
    }

    private fun getLocalizedPrayerName(context: Context, prayerName: String): String {
        return when (prayerName) {
            PrayerTimesMediumWidgetViewModel.Prayer.FAJR.name -> context.getString(com.ibadalrahman.resources.R.string.fajr)
            PrayerTimesMediumWidgetViewModel.Prayer.SUNRISE.name -> context.getString(com.ibadalrahman.resources.R.string.sunrise)
            PrayerTimesMediumWidgetViewModel.Prayer.DHUHR.name -> context.getString(com.ibadalrahman.resources.R.string.dhuhr)
            PrayerTimesMediumWidgetViewModel.Prayer.ASR.name -> context.getString(com.ibadalrahman.resources.R.string.asr)
            PrayerTimesMediumWidgetViewModel.Prayer.MAGHRIB.name -> context.getString(com.ibadalrahman.resources.R.string.maghrib)
            PrayerTimesMediumWidgetViewModel.Prayer.ISHAA.name -> context.getString(com.ibadalrahman.resources.R.string.ishaa)
            else -> prayerName
        }
    }

    private fun showErrorWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        errorMessage: String
    ) {
        val views = RemoteViews(context.packageName, R.layout.prayer_times_medium_widget_layout)

        // Apply RTL layout direction if current locale is RTL
        applyLayoutDirection(views)

        // Set up click intent to open the app
        val intent = Intent(context, Class.forName("org.ibadalrahman.publicsector.main.view.MainActivity"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        views.setTextViewText(R.id.gregorian_day, "")
        views.setTextViewText(R.id.gregorian_month, "")
        views.setTextViewText(R.id.gregorian_year, "")
        views.setTextViewText(R.id.hijri_day, "")
        views.setTextViewText(R.id.hijri_month, "")
        views.setTextViewText(R.id.hijri_year, "")
        views.setTextViewText(R.id.next_prayer_label, "")
        views.setTextViewText(R.id.next_prayer_name, "")
        views.setChronometer(R.id.next_prayer_time, 0, null, false)
        views.setTextViewText(R.id.text1, "Error:")
        views.setTextViewText(R.id.text2, errorMessage)
        views.setTextViewText(R.id.text3, "")
        views.setTextViewText(R.id.text4, "")
        views.setTextViewText(R.id.text5, "")
        views.setTextViewText(R.id.text6, "")
        views.setTextViewText(R.id.time1, "")
        views.setTextViewText(R.id.time2, "")
        views.setTextViewText(R.id.time3, "")
        views.setTextViewText(R.id.time4, "")
        views.setTextViewText(R.id.time5, "")
        views.setTextViewText(R.id.time6, "")
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun scheduleWidgetUpdate(context: Context, updateTimeMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Check if we can schedule exact alarms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w(TAG, "Cannot schedule exact alarms. Widget may not update automatically.")
                // Fall back to inexact alarm
                scheduleInexactUpdate(context, alarmManager, updateTimeMillis)
                return
            }
        }

        val intent = Intent(context, PrayerTimesMediumWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, PrayerTimesMediumWidgetProvider::class.java)
            )
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Cancel any existing alarm
        alarmManager.cancel(pendingIntent)

        // Set new alarm
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                updateTimeMillis,
                pendingIntent
            )
            Log.d(TAG, "Scheduled exact widget update at ${java.util.Date(updateTimeMillis)}")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to schedule exact alarm", e)
            scheduleInexactUpdate(context, alarmManager, updateTimeMillis)
        }
    }

    private fun scheduleInexactUpdate(context: Context, alarmManager: AlarmManager, updateTimeMillis: Long) {
        val intent = Intent(context, PrayerTimesMediumWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, PrayerTimesMediumWidgetProvider::class.java)
            )
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Use inexact alarm with a window
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            updateTimeMillis,
            5 * 60 * 1000, // 5 minute window
            pendingIntent
        )

        Log.d(TAG, "Scheduled inexact widget update around ${java.util.Date(updateTimeMillis)}")
    }
}
