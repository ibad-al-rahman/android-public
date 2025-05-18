package com.ibadalrahman.prayertimes.domain.entity

import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import java.util.Date

sealed interface PrayerTimesAction {
    data object ShowDatePicker: PrayerTimesAction
    data object HideDatePicker: PrayerTimesAction
    data object ShowDailyView: PrayerTimesAction
    data object ShowWeeklyView: PrayerTimesAction
    data class OnDateSelected(val date: Date): PrayerTimesAction
    data class LoadPrayerTimes(val date: Date): PrayerTimesAction
    data class Share(val state: PrayerTimesScreenState): PrayerTimesAction
}
