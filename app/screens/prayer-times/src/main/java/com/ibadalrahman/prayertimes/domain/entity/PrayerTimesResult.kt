package com.ibadalrahman.prayertimes.domain.entity

import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.domain.WeekPrayerTimes

sealed interface PrayerTimesResult {
    data object Loading: PrayerTimesResult
    data object ShowDatePicker: PrayerTimesResult
    data object HideDatePicker: PrayerTimesResult
    data object ShowDailyView: PrayerTimesResult
    data object ShowWeeklyView: PrayerTimesResult
    data class PrayerTimesLoaded(
        val prayerTimes: DayPrayerTimes,
        val weekPrayerTimes: WeekPrayerTimes
    ): PrayerTimesResult
}
