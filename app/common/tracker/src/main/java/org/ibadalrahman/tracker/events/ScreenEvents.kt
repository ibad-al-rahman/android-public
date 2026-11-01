package org.ibadalrahman.tracker.events

import org.ibadalrahman.tracker.entity.EventData
import org.ibadalrahman.tracker.entity.EventTrackerData

/** `screen_view` events. [screenName] is logged as the `screen_name` parameter. */
sealed class ScreenEvents(val screenName: String) : EventData {
    data object PrayerTimes : ScreenEvents("prayer_times")
    data object Adhkar : ScreenEvents("adhkar")
    data object Settings : ScreenEvents("settings")
    data object ForceUpdate : ScreenEvents("force_update")

    override fun toTrackingData(): EventTrackerData = EventTrackerData(
        eventName = "screen_view",
        params = listOf("screen_name" to screenName),
    )
}
