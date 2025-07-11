package org.ibadalrahman.widgets.prayertimes

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
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

class PrayerTimesSmallWidgetProvider: AppWidgetProvider() {
    companion object {
        private const val TAG = "PrayerTimesSmallWidget"
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
                val thisWidget = android.content.ComponentName(context, PrayerTimesSmallWidgetProvider::class.java)
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

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled - Last widget removed")
        job.cancel()
    }

    private suspend fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.prayer_times_small_widget_layout)

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
                    Log.d(TAG, "Successfully fetched prayer times")

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
                    } ?: run {
                        views.setTextViewText(R.id.next_prayer_label, "")
                        views.setTextViewText(R.id.next_prayer_name, "")
                        views.setChronometer(R.id.next_prayer_time, 0, null, false)
                    }
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to fetch prayer times", error)
                    views.setTextViewText(R.id.gregorian_day, "")
                    views.setTextViewText(R.id.gregorian_month, "Error")
                    views.setTextViewText(R.id.gregorian_year, "")
                    views.setTextViewText(R.id.hijri_day, "")
                    views.setTextViewText(R.id.hijri_month, "")
                    views.setTextViewText(R.id.hijri_year, "")
                    views.setTextViewText(R.id.next_prayer_label, "")
                    views.setTextViewText(R.id.next_prayer_name, "")
                    views.setChronometer(R.id.next_prayer_time, 0, null, false)
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
        val views = RemoteViews(context.packageName, R.layout.prayer_times_small_widget_layout)

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
        views.setTextViewText(R.id.gregorian_month, "Error")
        views.setTextViewText(R.id.gregorian_year, "")
        views.setTextViewText(R.id.hijri_day, "")
        views.setTextViewText(R.id.hijri_month, errorMessage)
        views.setTextViewText(R.id.hijri_year, "")
        views.setTextViewText(R.id.next_prayer_label, "")
        views.setTextViewText(R.id.next_prayer_name, "")
        views.setChronometer(R.id.next_prayer_time, 0, null, false)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
