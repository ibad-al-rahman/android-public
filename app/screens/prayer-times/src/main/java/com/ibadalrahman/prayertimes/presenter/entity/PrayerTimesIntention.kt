package com.ibadalrahman.prayertimes.presenter.entity

import java.util.Date

sealed interface PrayerTimesIntention {
    data object OnScreenStarted: PrayerTimesIntention
    data object OnTapShowDatePicker: PrayerTimesIntention
    data object OnDismissDatePicker: PrayerTimesIntention
    data object OnTapDailyView: PrayerTimesIntention
    data object OnTapWeeklyView: PrayerTimesIntention
    data class OnDateSelected(val date: Date): PrayerTimesIntention
}
