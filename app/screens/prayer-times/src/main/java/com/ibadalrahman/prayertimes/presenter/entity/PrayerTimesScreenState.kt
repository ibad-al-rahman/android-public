package com.ibadalrahman.prayertimes.presenter.entity

import java.util.Date

data class PrayerTimesScreenState(
    val isLoading: Boolean,
    val date: Date
) {
    companion object {
        val initialState: PrayerTimesScreenState
            get() = PrayerTimesScreenState(
                isLoading = false,
                date = Date()
            )
    }
}
