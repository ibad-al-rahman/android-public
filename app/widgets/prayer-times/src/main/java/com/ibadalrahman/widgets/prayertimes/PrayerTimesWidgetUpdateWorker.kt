package com.ibadalrahman.widgets.prayertimes

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

@HiltWorker
class PrayerTimesWidgetUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface PrayerTimesWorkerEntryPoint {
        fun prayerTimesRepository(): PrayerTimesRepository
    }

    override suspend fun doWork(): Result {
        return try {
            PrayerTimesWidgetRootScreen().updateAll(applicationContext)
            scheduleNextPrayerUpdate(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun scheduleNextPrayerUpdate(context: Context) {
        val entryPoint = EntryPoints.get(
            context.applicationContext,
            PrayerTimesWorkerEntryPoint::class.java
        )
        val repository = entryPoint.prayerTimesRepository()

        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.time = now
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val todayPrayerTimes = repository.getDayPrayerTimes(year, month, day).getOrNull()
        val tomorrowCalendar = Calendar.getInstance().apply {
            time = now
            add(Calendar.DAY_OF_MONTH, 1)
        }
        val tomorrowPrayerTimes = repository.getDayPrayerTimes(
            tomorrowCalendar.get(Calendar.YEAR),
            tomorrowCalendar.get(Calendar.MONTH) + 1,
            tomorrowCalendar.get(Calendar.DAY_OF_MONTH)
        ).getOrNull()

        if (todayPrayerTimes != null) {
            val nextPrayerTime = findNextPrayerTime(now, todayPrayerTimes, tomorrowPrayerTimes)

            if (nextPrayerTime != null) {
                val delayMillis = nextPrayerTime.time - now.time

                if (delayMillis > 0) {
                    val workRequest = OneTimeWorkRequestBuilder<PrayerTimesWidgetUpdateWorker>()
                        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                        .addTag(NEXT_PRAYER_UPDATE_TAG)
                        .build()

                    WorkManager.getInstance(context)
                        .enqueue(workRequest)
                }
            }
        }
    }

    private fun findNextPrayerTime(
        currentTime: Date,
        todayPrayerTimes: DayPrayerTimes,
        tomorrowPrayerTimes: DayPrayerTimes?
    ): Date? {
        val prayerTimes = listOf(
            todayPrayerTimes.prayerTimes.fajr,
            todayPrayerTimes.prayerTimes.sunrise,
            todayPrayerTimes.prayerTimes.dhuhr,
            todayPrayerTimes.prayerTimes.asr,
            todayPrayerTimes.prayerTimes.maghrib,
            todayPrayerTimes.prayerTimes.ishaa
        )

        for (prayerTime in prayerTimes) {
            if (prayerTime.after(currentTime)) {
                return prayerTime
            }
        }

        return tomorrowPrayerTimes?.prayerTimes?.fajr
    }

    companion object {
        const val NEXT_PRAYER_UPDATE_TAG = "next_prayer_update"
    }
}
