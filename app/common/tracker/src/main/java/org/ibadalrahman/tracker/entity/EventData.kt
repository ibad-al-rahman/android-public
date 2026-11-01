package org.ibadalrahman.tracker.entity

/**
 * Implemented by every analytics event (modelled as sealed classes) so the [Tracker] can log them
 * without knowing their concrete type. Mirrors the Proximie Android `EventData` pattern.
 */
interface EventData {
    fun toTrackingData(): EventTrackerData
}
