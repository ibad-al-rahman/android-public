package com.ibadalrahman.prayertimes.presenter.entity

sealed interface PrayerTimesIntention {
    data object OnScreenStarted: PrayerTimesIntention
    data object OnTapShowDatePicker: PrayerTimesIntention
    data object OnDismissDatePicker: PrayerTimesIntention
}
