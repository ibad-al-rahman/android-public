package org.ibadalrahman.widgets.prayertimes

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
        fun viewModel(): PrayerTimesWidgetViewModel
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            Intent.ACTION_LOCALE_CHANGED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> {
                Log.d(TAG, "System settings changed (${intent.action}), updating all widgets")
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

        PrayerTimesWidgetUpdateWorker.scheduleUpdates(context)

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
        PrayerTimesWidgetUpdateWorker.scheduleUpdates(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled - Last widget removed")
        job.cancel()
        PrayerTimesWidgetUpdateWorker.cancelUpdates(context)

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

        applyLayoutDirection(views)

        val intent = Intent(context, Class.forName("org.ibadalrahman.publicsector.main.view.MainActivity"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        val configuration = context.resources.configuration
        val isNightMode = (configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
        val defaultTextColor = if (isNightMode) android.graphics.Color.WHITE else android.graphics.Color.BLACK

        try {
            Log.d(TAG, "Getting ViewModel from EntryPoint")

            val entryPoint = EntryPoints.get(
                context.applicationContext,
                WidgetEntryPoint::class.java
            )
            val viewModel = entryPoint.viewModel()

            Log.d(TAG, "Fetching prayer times from ViewModel")

            viewModel.getPrayerTimes().fold(
                onSuccess = { prayerData ->
                    Log.d(TAG, "Successfully fetched prayer times: ${prayerData.prayerTimes.size} prayers")

                    views.setTextViewText(R.id.gregorian_day, prayerData.gregorianDate.day)
                    views.setTextViewText(R.id.gregorian_month, prayerData.gregorianDate.month)
                    views.setTextViewText(R.id.gregorian_year, prayerData.gregorianDate.year)

                    views.setTextViewText(R.id.hijri_day, prayerData.hijriDate.day)
                    views.setTextViewText(R.id.hijri_month, prayerData.hijriDate.month)
                    views.setTextViewText(R.id.hijri_year, prayerData.hijriDate.year)

                    prayerData.nextPrayer?.let { nextPrayer ->
                        val prayerName = getLocalizedPrayerName(context, nextPrayer.prayerName)
                        val afterText = context.getString(org.ibadalrahman.resources.R.string.prayer_after)

                        views.setTextViewText(R.id.next_prayer_label, prayerName)
                        views.setTextViewText(R.id.next_prayer_name, " $afterText")
                        views.setChronometerCountDown(R.id.next_prayer_time, true)
                        views.setChronometer(R.id.next_prayer_time, nextPrayer.chronometerBaseTime, null, true)

                        val updateTime = System.currentTimeMillis() + (nextPrayer.chronometerBaseTime - android.os.SystemClock.elapsedRealtime())
                        scheduleWidgetUpdate(context, updateTime)
                    } ?: run {
                        views.setTextViewText(R.id.next_prayer_label, "")
                        views.setTextViewText(R.id.next_prayer_name, "")
                        views.setChronometer(R.id.next_prayer_time, 0, null, false)
                    }

                    views.setInt(R.id.fajr_row, "setBackgroundResource", 0)
                    views.setInt(R.id.sunrise_row, "setBackgroundResource", 0)
                    views.setInt(R.id.dhuhr_row, "setBackgroundResource", 0)
                    views.setInt(R.id.asr_row, "setBackgroundResource", 0)
                    views.setInt(R.id.maghrib_row, "setBackgroundResource", 0)
                    views.setInt(R.id.ishaa_row, "setBackgroundResource", 0)

                    val prayers = PrayerTimesWidgetViewModel.Prayer.entries.toTypedArray()
                    prayers.forEach { prayer ->
                        val time = prayerData.prayerTimes[prayer] ?: ""

                        Log.d(TAG, "Prayer ${prayer.name}: $time")

                        val isCurrentPrayer = prayerData.currentPrayer == prayer
                        if (isCurrentPrayer) {
                            val rowId = when (prayer) {
                                PrayerTimesWidgetViewModel.Prayer.FAJR -> R.id.fajr_row
                                PrayerTimesWidgetViewModel.Prayer.SUNRISE -> R.id.sunrise_row
                                PrayerTimesWidgetViewModel.Prayer.DHUHR -> R.id.dhuhr_row
                                PrayerTimesWidgetViewModel.Prayer.ASR -> R.id.asr_row
                                PrayerTimesWidgetViewModel.Prayer.MAGHRIB -> R.id.maghrib_row
                                PrayerTimesWidgetViewModel.Prayer.ISHAA -> R.id.ishaa_row
                            }
                            views.setInt(rowId, "setBackgroundResource", R.drawable.prayer_time_highlight_background)
                        }

                        val (textId, timeId) = when (prayer) {
                            PrayerTimesWidgetViewModel.Prayer.FAJR -> R.id.fajr_text to R.id.fajr_time
                            PrayerTimesWidgetViewModel.Prayer.SUNRISE -> R.id.sunrise_text to R.id.sunrise_time
                            PrayerTimesWidgetViewModel.Prayer.DHUHR -> R.id.dhuhr_text to R.id.dhuhr_time
                            PrayerTimesWidgetViewModel.Prayer.ASR -> R.id.asr_text to R.id.asr_time
                            PrayerTimesWidgetViewModel.Prayer.MAGHRIB -> R.id.maghrib_text to R.id.maghrib_time
                            PrayerTimesWidgetViewModel.Prayer.ISHAA -> R.id.ishaa_text to R.id.ishaa_time
                        }

                        views.setTextViewText(textId, context.getString(prayer.stringResId))
                        views.setTextViewText(timeId, time)

                        if (isCurrentPrayer) {
                            views.setTextColor(textId, android.graphics.Color.WHITE)
                            views.setTextColor(timeId, android.graphics.Color.WHITE)
                        } else {
                            views.setTextColor(textId, defaultTextColor)
                            views.setTextColor(timeId, defaultTextColor)
                        }
                    }
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to fetch prayer times", error)
                    views.setTextViewText(R.id.gregorian_day, "")
                    views.setTextViewText(R.id.gregorian_month, "")
                    views.setTextViewText(R.id.gregorian_year, "")
                    views.setTextViewText(R.id.hijri_day, "")
                    views.setTextViewText(R.id.hijri_month, "")
                    views.setTextViewText(R.id.hijri_year, "")
                    views.setTextViewText(R.id.next_prayer_label, "")
                    views.setTextViewText(R.id.next_prayer_name, "")
                    views.setChronometer(R.id.next_prayer_time, 0, null, false)
                    views.setTextViewText(R.id.fajr_text, "Error loading")
                    views.setTextViewText(R.id.sunrise_text, "prayer times")
                    views.setTextViewText(R.id.dhuhr_text, error.message ?: "")
                    views.setTextViewText(R.id.asr_text, "")
                    views.setTextViewText(R.id.maghrib_text, "")
                    views.setTextViewText(R.id.ishaa_text, "")
                    views.setTextViewText(R.id.fajr_time, "")
                    views.setTextViewText(R.id.sunrise_time, "")
                    views.setTextViewText(R.id.dhuhr_time, "")
                    views.setTextViewText(R.id.asr_time, "")
                    views.setTextViewText(R.id.maghrib_time, "")
                    views.setTextViewText(R.id.ishaa_time, "")
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
            views.setInt(R.id.widget_root, "setLayoutDirection", View.LAYOUT_DIRECTION_RTL)
        } else {
            views.setInt(R.id.widget_root, "setLayoutDirection", View.LAYOUT_DIRECTION_LTR)
        }
    }

    private fun getLocalizedPrayerName(context: Context, prayerName: String): String {
        return when (prayerName) {
            PrayerTimesWidgetViewModel.Prayer.FAJR.name -> context.getString(org.ibadalrahman.resources.R.string.fajr)
            PrayerTimesWidgetViewModel.Prayer.SUNRISE.name -> context.getString(org.ibadalrahman.resources.R.string.sunrise)
            PrayerTimesWidgetViewModel.Prayer.DHUHR.name -> context.getString(org.ibadalrahman.resources.R.string.dhuhr)
            PrayerTimesWidgetViewModel.Prayer.ASR.name -> context.getString(org.ibadalrahman.resources.R.string.asr)
            PrayerTimesWidgetViewModel.Prayer.MAGHRIB.name -> context.getString(org.ibadalrahman.resources.R.string.maghrib)
            PrayerTimesWidgetViewModel.Prayer.ISHAA.name -> context.getString(org.ibadalrahman.resources.R.string.ishaa)
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

        applyLayoutDirection(views)

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
        views.setTextViewText(R.id.fajr_text, "Error:")
        views.setTextViewText(R.id.sunrise_text, errorMessage)
        views.setTextViewText(R.id.dhuhr_text, "")
        views.setTextViewText(R.id.asr_text, "")
        views.setTextViewText(R.id.maghrib_text, "")
        views.setTextViewText(R.id.ishaa_text, "")
        views.setTextViewText(R.id.fajr_time, "")
        views.setTextViewText(R.id.sunrise_time, "")
        views.setTextViewText(R.id.dhuhr_time, "")
        views.setTextViewText(R.id.asr_time, "")
        views.setTextViewText(R.id.maghrib_time, "")
        views.setTextViewText(R.id.ishaa_time, "")
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun scheduleWidgetUpdate(context: Context, updateTimeMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w(TAG, "Cannot schedule exact alarms. Widget may not update automatically.")
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

        alarmManager.cancel(pendingIntent)

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

        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            updateTimeMillis,
            5 * 60 * 1000, // 5 minute window
            pendingIntent
        )

        Log.d(TAG, "Scheduled inexact widget update around ${java.util.Date(updateTimeMillis)}")
    }
}
