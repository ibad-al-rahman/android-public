package com.ibadalrahman.prayertimes.domain.entity

sealed interface PrayerTimesAction {
    data object ShowDatePicker: PrayerTimesAction
    data object HideDatePicker: PrayerTimesAction
    data class LoadPrayerTimes(val year: Int): PrayerTimesAction
}
