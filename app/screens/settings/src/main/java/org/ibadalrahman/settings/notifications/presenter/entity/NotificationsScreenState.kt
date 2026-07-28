package org.ibadalrahman.settings.notifications.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * UI state for the Notifications screen.
 *
 * NOTE: this is a UI shell — nothing here is persisted yet. State lives only in the ViewModel
 * for the lifetime of the screen. Wiring persistence and the POST_NOTIFICATIONS permission
 * request is a follow-up. See [org.ibadalrahman.settings.notifications.domain.NotificationsInteractor].
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
        val Default = NotificationsScreenState(
            notificationsEnabled = false,
            fajr = false,
            dhuhr = false,
            asr = false,
            maghrib = false,
            ishaa = false,
            morningAdhkarEnabled = false,
            eveningAdhkarEnabled = false,
            morningHour = 6,
            morningMinute = 0,
            eveningHour = 18,
            eveningMinute = 0,
        )
    }
}
