package org.ibadalrahman.settings.notifications.domain.entity

/**
 * Persistence actions for the Notifications screen. Each maps to a mutation of the persisted
 * [org.ibadalrahman.settings.repository.data.domain.NotificationSettings]; [Load] just reloads the
 * current snapshot into state.
 */
sealed interface NotificationsAction {
    data object Load : NotificationsAction
    data class SetNotificationsEnabled(val enabled: Boolean) : NotificationsAction
    data class SetFajr(val enabled: Boolean) : NotificationsAction
    data class SetDhuhr(val enabled: Boolean) : NotificationsAction
    data class SetAsr(val enabled: Boolean) : NotificationsAction
    data class SetMaghrib(val enabled: Boolean) : NotificationsAction
    data class SetIshaa(val enabled: Boolean) : NotificationsAction
    data class SetMorningAdhkarEnabled(val enabled: Boolean) : NotificationsAction
    data class SetEveningAdhkarEnabled(val enabled: Boolean) : NotificationsAction
    data class SetMorningTime(val hour: Int, val minute: Int) : NotificationsAction
    data class SetEveningTime(val hour: Int, val minute: Int) : NotificationsAction

    /** Posts a notification immediately for manual testing; persists nothing. */
    data object SendTestNotification : NotificationsAction
}
