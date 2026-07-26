package org.ibadalrahman.miqat.repository.data.domain

import org.ibadalrahman.miqat.IslamicEvent
import org.ibadalrahman.miqat.IslamicEventOccurrence
import java.util.Date

/**
 * An occurrence of an Islamic event with its Gregorian and Hijri dates.
 *
 * Port of iOS `MiqatEventOccurrence`.
 */
data class MiqatEventOccurrence(
    val event: IslamicEvent,
    val gregorianDate: Date,
    val hijriDate: MiqatHijriDate,
) {
    companion object {
        fun from(occurrence: IslamicEventOccurrence): MiqatEventOccurrence = MiqatEventOccurrence(
            event = occurrence.event,
            gregorianDate = Date(occurrence.gregorianTimestampSecs * 1000),
            hijriDate = MiqatHijriDate.from(occurrence.hijriDate),
        )
    }
}
