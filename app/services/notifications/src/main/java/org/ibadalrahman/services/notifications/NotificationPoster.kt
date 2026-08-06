package org.ibadalrahman.services.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ibadalrahman.resources.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Creates the notification channels and posts prayer/adhkar notifications. Channels are created
 * unconditionally (minSdk 26 always has channels); posting is a no-op if the user revoked
 * POST_NOTIFICATIONS on Android 13+.
 */
@Singleton
class NotificationPoster @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    /** Idempotent — safe to call on every app start. */
    fun ensureChannels() {
        val manager = context.getSystemService<NotificationManager>() ?: return

        // The adhan sound is a channel property, set once at creation (setSound on the builder is
        // ignored on API 26+). It is immutable afterwards — changing it later needs a new channel id.
        val adhanUri = "android.resource://${context.packageName}/${R.raw.azan}".toUri()
        val adhanAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_PRAYERS,
                context.getString(R.string.notification_channel_prayers),
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = context.getString(R.string.notification_channel_prayers_description)
                setSound(adhanUri, adhanAttributes)
            }
        )
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ADHKAR,
                context.getString(R.string.notification_channel_adhkar),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = context.getString(R.string.notification_channel_adhkar_description)
            }
        )
    }

    fun post(type: NotificationEventType) {
        val (channelId, title, body) = content(type)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = NotificationManagerCompat.from(context)
        if (manager.areNotificationsEnabled()) {
            manager.notify(type.ordinal, notification)
        }
    }

    private data class Content(val channelId: String, val title: String, val body: String)

    private fun content(type: NotificationEventType): Content = when (type) {
        NotificationEventType.FAJR -> prayerContent(R.string.fajr)
        NotificationEventType.DHUHR -> prayerContent(R.string.dhuhr)
        NotificationEventType.ASR -> prayerContent(R.string.asr)
        NotificationEventType.MAGHRIB -> prayerContent(R.string.maghrib)
        NotificationEventType.ISHAA -> prayerContent(R.string.ishaa)
        NotificationEventType.MORNING_ADHKAR -> Content(
            channelId = CHANNEL_ADHKAR,
            title = context.getString(R.string.morning_adhkar),
            body = context.getString(R.string.notification_morning_adhkar_body),
        )
        NotificationEventType.EVENING_ADHKAR -> Content(
            channelId = CHANNEL_ADHKAR,
            title = context.getString(R.string.evening_adhkar),
            body = context.getString(R.string.notification_evening_adhkar_body),
        )
    }

    private fun prayerContent(prayerNameRes: Int): Content {
        val name = context.getString(prayerNameRes)
        return Content(
            channelId = CHANNEL_PRAYERS,
            title = name,
            body = context.getString(R.string.notification_prayer_body, name),
        )
    }

    private companion object {
        const val CHANNEL_PRAYERS = "prayers"
        const val CHANNEL_ADHKAR = "adhkar"
    }
}
