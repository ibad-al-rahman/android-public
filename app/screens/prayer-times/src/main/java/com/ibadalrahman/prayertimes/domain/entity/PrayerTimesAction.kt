package com.ibadalrahman.prayertimes.domain.entity

import java.util.Date

sealed interface PrayerTimesAction {
    data object ShowDatePicker: PrayerTimesAction
    data object HideDatePicker: PrayerTimesAction
    data class LoadPrayerTimes(val date: Date): PrayerTimesAction
}
