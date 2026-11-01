package org.ibadalrahman.tracker.events

import org.ibadalrahman.tracker.entity.EventData
import org.ibadalrahman.tracker.entity.EventTrackerData

sealed class ForceUpdateEvents(val eventName: String) : EventData {
    /** The blocking force-update screen was shown to the user. */
    data object Shown : ForceUpdateEvents("force_update_shown")

    /** The user tapped "Update Now" on the force-update screen. */
    data object UpdateTapped : ForceUpdateEvents("force_update_tapped")

    override fun toTrackingData(): EventTrackerData = EventTrackerData(eventName = eventName)
}
