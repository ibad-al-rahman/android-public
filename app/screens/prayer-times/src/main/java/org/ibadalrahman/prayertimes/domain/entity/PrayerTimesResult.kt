package org.ibadalrahman.prayertimes.domain.entity

import org.ibadalrahman.miqat.repository.data.domain.MiqatData

sealed interface PrayerTimesResult {
    data object ShowDatePicker: PrayerTimesResult
    data object HideDatePicker: PrayerTimesResult
    data object ShowDailyView: PrayerTimesResult
    data object ShowWeeklyView: PrayerTimesResult
    data class PrayerTimesLoaded(
        val day: MiqatData,
        /** The day's Hijri date, already formatted and digit-localized for display. */
        val dayHijri: String,
        /** The day's Islamic event name, already localized, or null when there is none. */
        val event: String?,
        /** The seven days of the containing Sat→Fri week (null slots when a day is unavailable). */
        val week: List<MiqatData?>,
    ): PrayerTimesResult
    data class ShareTextProcessed(val text: String): PrayerTimesResult
}
