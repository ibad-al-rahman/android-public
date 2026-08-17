package org.ibadalrahman.settings.notifications.presenter.entity

sealed interface NotificationsIntention {
    data object Load : NotificationsIntention
    data class SetNotificationsEnabled(val enabled: Boolean) : NotificationsIntention
    data class SetFajr(val enabled: Boolean) : NotificationsIntention
    data class SetDhuhr(val enabled: Boolean) : NotificationsIntention
    data class SetAsr(val enabled: Boolean) : NotificationsIntention
    data class SetMaghrib(val enabled: Boolean) : NotificationsIntention
    data class SetIshaa(val enabled: Boolean) : NotificationsIntention
    data class SetMorningAdhkarEnabled(val enabled: Boolean) : NotificationsIntention
    data class SetEveningAdhkarEnabled(val enabled: Boolean) : NotificationsIntention
    data class SetMorningTime(val hour: Int, val minute: Int) : NotificationsIntention
    data class SetEveningTime(val hour: Int, val minute: Int) : NotificationsIntention

    /** Posts a notification immediately for manual testing — bypasses scheduling. */
    data object SendTestNotification : NotificationsIntention
}
