package org.ibadalrahman.settings.notifications.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.ibadalrahman.settings.repository.data.domain.NotificationSettings

/**
 * UI state for the Notifications screen. Persisted via [NotificationSettings]; the screen seeds
 * itself from the stored snapshot on resume and re-renders from it after every toggle.
 */
@Stable
@Immutable
data class NotificationsScreenState(
    val notificationsEnabled: Boolean,
    val fajr: Boolean,
    val dhuhr: Boolean,
    val asr: Boolean,
    val maghrib: Boolean,
    val ishaa: Boolean,
    val morningAdhkarEnabled: Boolean,
    val eveningAdhkarEnabled: Boolean,
    val morningHour: Int,
    val morningMinute: Int,
    val eveningHour: Int,
    val eveningMinute: Int,
) {
    companion object {
        val Default = fromSettings(NotificationSettings.Default)

        fun fromSettings(settings: NotificationSettings) = NotificationsScreenState(
            notificationsEnabled = settings.enabled,
            fajr = settings.fajr,
            dhuhr = settings.dhuhr,
            asr = settings.asr,
            maghrib = settings.maghrib,
            ishaa = settings.ishaa,
            morningAdhkarEnabled = settings.morningAdhkarEnabled,
            eveningAdhkarEnabled = settings.eveningAdhkarEnabled,
            morningHour = settings.morningHour,
            morningMinute = settings.morningMinute,
            eveningHour = settings.eveningHour,
            eveningMinute = settings.eveningMinute,
        )
    }
}
