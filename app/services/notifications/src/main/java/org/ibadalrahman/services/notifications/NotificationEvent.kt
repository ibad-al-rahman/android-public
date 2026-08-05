package org.ibadalrahman.services.notifications

/** The kind of notification a scheduled instant will post. */
enum class NotificationEventType {
    FAJR,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHAA,
    MORNING_ADHKAR,
    EVENING_ADHKAR,
}

/** A candidate notification: a wall-clock instant (epoch millis) and what it would post. */
data class NotificationEvent(
    val type: NotificationEventType,
    val timeMillis: Long,
)
