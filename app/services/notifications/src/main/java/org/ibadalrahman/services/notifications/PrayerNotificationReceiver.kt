package org.ibadalrahman.services.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.EntryPoints

/**
 * Target of the rolling alarm. Posts the notification(s) that just came due, then re-arms the next
 * alarm. The due event types travel in [NotificationScheduler.EXTRA_EVENT_TYPES] so the receiver
 * knows what to post without recomputing.
 */
class PrayerNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != NotificationScheduler.ACTION_FIRE) return

        val typeOrdinals = intent.getIntArrayExtra(NotificationScheduler.EXTRA_EVENT_TYPES)
            ?: IntArray(0)
        val entryPoint = EntryPoints.get(context.applicationContext, NotificationEntryPoint::class.java)

        val pending = goAsync()
        Thread {
            try {
                val values = NotificationEventType.entries
                typeOrdinals.forEach { ordinal ->
                    values.getOrNull(ordinal)?.let { entryPoint.poster().post(it) }
                }
                entryPoint.scheduler().reschedule()
            } finally {
                pending.finish()
            }
        }.start()
    }
}
