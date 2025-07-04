package org.ibadalrahman.prayertimes.domain.entity

import org.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import org.ibadalrahman.prayertimes.repository.data.domain.WeekPrayerTimes

sealed interface PrayerTimesResult {
    data object Loading: PrayerTimesResult
    data object UnknownError: PrayerTimesResult
    data object ShowDatePicker: PrayerTimesResult
    data object HideDatePicker: PrayerTimesResult
    data object ShowDailyView: PrayerTimesResult
    data object ShowWeeklyView: PrayerTimesResult
    data class PrayerTimesLoaded(
        val prayerTimes: DayPrayerTimes,
        val weekPrayerTimes: WeekPrayerTimes
    ): PrayerTimesResult
    data class ShareTextProcessed(val text: String): PrayerTimesResult
}
