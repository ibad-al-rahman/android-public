package com.ibadalrahman.prayertimes.domain.entity

sealed interface PrayerTimesResult {
    data object Noop: PrayerTimesResult
}
