package com.ibadalrahman.widgets.prayertimes

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrayerTimesWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PrayerTimesWidgetRootScreen()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Schedule initial update immediately
        scheduleImmediateUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Cancel all widget update work
        WorkManager.getInstance(context).cancelAllWorkByTag(PrayerTimesWidgetUpdateWorker.NEXT_PRAYER_UPDATE_TAG)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            // Schedule immediate update when widget is added or updated
            scheduleImmediateUpdate(context)
        }
    }

    private fun scheduleImmediateUpdate(context: Context) {
        // Cancel any existing scheduled work
        WorkManager.getInstance(context).cancelAllWorkByTag(PrayerTimesWidgetUpdateWorker.NEXT_PRAYER_UPDATE_TAG)
        
        // Schedule immediate update
        val workRequest = OneTimeWorkRequestBuilder<PrayerTimesWidgetUpdateWorker>()
            .addTag(PrayerTimesWidgetUpdateWorker.NEXT_PRAYER_UPDATE_TAG)
            .build()
            
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
