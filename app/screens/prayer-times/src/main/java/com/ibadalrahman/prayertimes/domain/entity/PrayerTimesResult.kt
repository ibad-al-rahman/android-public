package com.ibadalrahman.prayertimes.domain.entity

import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes

sealed interface PrayerTimesResult {
    data object Noop: PrayerTimesResult
    data object Loading: PrayerTimesResult
    data class PrayerTimesLoaded(val prayerTimes: DayPrayerTimes): PrayerTimesResult
}
