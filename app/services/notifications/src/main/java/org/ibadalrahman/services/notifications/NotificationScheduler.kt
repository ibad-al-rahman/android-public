package org.ibadalrahman.services.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ibadalrahman.miqat.repository.MiqatRepository
import org.ibadalrahman.settings.repository.SettingsRepository
import org.ibadalrahman.settings.repository.data.domain.NotificationSettings
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The rolling-alarm engine. Reads the persisted [NotificationSettings] and prayer times from
 * [MiqatRepository], picks the next batch of events (via [NextEventSelector]), and arms a single
 * exact alarm for it — mirroring the widget's `scheduleWidgetUpdate` exact/inexact pattern.
 *
 * On fire, [PrayerNotificationReceiver] posts the batch and calls [reschedule] again, so exactly
 * one alarm is ever pending. [reschedule] is also the re-arm entry point after boot, a settings
 * change, or a timezone/clock change.
 */
@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val miqatRepository: MiqatRepository,
    private val settingsRepository: SettingsRepository,
) {

    /** Recompute the next batch from persisted settings and (re)arm or cancel the alarm. */
    fun reschedule() {
        val settings = settingsRepository.getNotificationSettings()
        if (!settings.enabled) {
            cancel()
            return
        }

        val now = System.currentTimeMillis()
        val batch = NextEventSelector.selectNextBatch(candidates(settings, now), now)
        if (batch.isEmpty()) {
            cancel()
            return
        }

        arm(batch)
    }

    /** Candidate instants across today and tomorrow for every enabled notification. */
    private fun candidates(settings: NotificationSettings, now: Long): List<NotificationEvent> {
        val days = listOf(dayMillis(now, 0), dayMillis(now, 1))
        return days.flatMap { candidatesForDay(settings, it) }
    }

    private fun candidatesForDay(settings: NotificationSettings, dayMillis: Long): List<NotificationEvent> {
        val events = mutableListOf<NotificationEvent>()

        val miqat = miqatRepository.getMiqatData(timestampSecs = middayEpochSecs(dayMillis))
        if (settings.fajr) events += NotificationEvent(NotificationEventType.FAJR, miqat.fajr.time)
        if (settings.dhuhr) events += NotificationEvent(NotificationEventType.DHUHR, miqat.dhuhr.time)
        if (settings.asr) events += NotificationEvent(NotificationEventType.ASR, miqat.asr.time)
        if (settings.maghrib) events += NotificationEvent(NotificationEventType.MAGHRIB, miqat.maghrib.time)
        if (settings.ishaa) events += NotificationEvent(NotificationEventType.ISHAA, miqat.ishaa.time)

        if (settings.morningAdhkarEnabled) {
            events += NotificationEvent(
                NotificationEventType.MORNING_ADHKAR,
                timeOfDay(dayMillis, settings.morningHour, settings.morningMinute),
            )
        }
        if (settings.eveningAdhkarEnabled) {
            events += NotificationEvent(
                NotificationEventType.EVENING_ADHKAR,
                timeOfDay(dayMillis, settings.eveningHour, settings.eveningMinute),
            )
        }
        return events
    }

    private fun arm(batch: List<NotificationEvent>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAt = batch.first().timeMillis
        val pendingIntent = firePendingIntent(batch.map { it.type })

        alarmManager.cancel(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Log.w(TAG, "Cannot schedule exact alarms; using an inexact window.")
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, triggerAt, INEXACT_WINDOW_MILLIS, pendingIntent)
            return
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            Log.d(TAG, "Scheduled notification(s) at ${Date(triggerAt)}: ${batch.map { it.type }}")
        } catch (e: SecurityException) {
            Log.e(TAG, "Exact alarm denied; using an inexact window.", e)
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, triggerAt, INEXACT_WINDOW_MILLIS, pendingIntent)
        }
    }

    private fun cancel() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(firePendingIntent(emptyList()))
    }

    private fun firePendingIntent(types: List<NotificationEventType>): PendingIntent {
        val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
            action = ACTION_FIRE
            putExtra(EXTRA_EVENT_TYPES, IntArray(types.size) { types[it].ordinal })
        }
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    /** Epoch millis at midnight of the day [offsetDays] away from [fromMillis], local time. */
    private fun dayMillis(fromMillis: Long, offsetDays: Int): Long =
        Calendar.getInstance().apply {
            timeInMillis = fromMillis
            add(Calendar.DAY_OF_MONTH, offsetDays)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    /** Epoch seconds at 12:00 local time of the day containing [dayMillis] — miqat keys by day. */
    private fun middayEpochSecs(dayMillis: Long): Long =
        Calendar.getInstance().apply {
            timeInMillis = dayMillis
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis / 1000

    /** Epoch millis at [hour]:[minute] local time of the day containing [dayMillis]. */
    private fun timeOfDay(dayMillis: Long, hour: Int, minute: Int): Long =
        Calendar.getInstance().apply {
            timeInMillis = dayMillis
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    companion object {
        private const val TAG = "NotificationScheduler"
        private const val REQUEST_CODE = 0xADAB
        private const val INEXACT_WINDOW_MILLIS = 5 * 60 * 1000L

        const val ACTION_FIRE = "org.ibadalrahman.services.notifications.ACTION_FIRE"
        const val EXTRA_EVENT_TYPES = "org.ibadalrahman.services.notifications.EXTRA_EVENT_TYPES"
    }
}
