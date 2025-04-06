package com.ibadalrahman.prayertimes.presenter

import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState

object PrayerTimesReducer {
    fun reduce(
        prevState: PrayerTimesScreenState,
        result: PrayerTimesResult
    ): PrayerTimesScreenState = when(result) {
        else -> {
            println(result)
            PrayerTimesScreenState.initialState
        }
    }
}
