package org.ibadalrahman.settings.repository.data.domain

/**
 * Persisted local-notification preferences. Mirrors the Notifications settings screen: a master
 * toggle, a per-prayer toggle for each of the five daily prayers (sunrise is never notified), and
 * morning/evening adhkar toggles with their own time-of-day.
 *
 * The rolling-alarm scheduler reads a snapshot of this to decide the next instant to fire.
 */
data class NotificationSettings(
    val enabled: Boolean,
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
        val Default = NotificationSettings(
            enabled = false,
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
