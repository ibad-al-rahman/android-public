package com.ibadalrahman.widgets.prayertimes

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PrayerTimesWidgetUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            PrayerTimesWidgetRootScreen().updateAll(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
