package com.ibadalrahman.prayertimes.domain.entity

sealed interface PrayerTimesAction {
    data object Setup: PrayerTimesAction
}
