package org.ibadalrahman.services.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.EntryPoints

/**
 * Re-arms the rolling alarm after events that invalidate it: a reboot (which clears all alarms) or
 * a wall-clock/timezone change (which shifts every prayer instant). Never posts a notification —
 * it only reschedules.
 */
class BootAndTimeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_TIME_CHANGED -> Unit
            else -> return
        }

        val entryPoint = EntryPoints.get(context.applicationContext, NotificationEntryPoint::class.java)
        val pending = goAsync()
        Thread {
            try {
                entryPoint.scheduler().reschedule()
            } finally {
                pending.finish()
            }
        }.start()
    }
}
