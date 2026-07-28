package org.ibadalrahman.settings.notifications.domain.entity

sealed interface NotificationsResult {
    data class NotificationsEnabled(val enabled: Boolean) : NotificationsResult
    data class Fajr(val enabled: Boolean) : NotificationsResult
    data class Dhuhr(val enabled: Boolean) : NotificationsResult
    data class Asr(val enabled: Boolean) : NotificationsResult
    data class Maghrib(val enabled: Boolean) : NotificationsResult
    data class Ishaa(val enabled: Boolean) : NotificationsResult
    data class MorningAdhkarEnabled(val enabled: Boolean) : NotificationsResult
    data class EveningAdhkarEnabled(val enabled: Boolean) : NotificationsResult
    data class MorningTime(val hour: Int, val minute: Int) : NotificationsResult
    data class EveningTime(val hour: Int, val minute: Int) : NotificationsResult
}
