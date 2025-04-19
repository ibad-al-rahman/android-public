package com.ibadalrahman.prayertimes.presenter

import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState

object PrayerTimesReducer {
    fun reduce(
        prevState: PrayerTimesScreenState,
        result: PrayerTimesResult
    ): PrayerTimesScreenState = when(result) {
        PrayerTimesResult.Loading -> prevState.copy(isLoading = true)
        PrayerTimesResult.ShowDatePicker -> prevState.copy(isDatePickerVisible = true)
        PrayerTimesResult.HideDatePicker -> prevState.copy(isDatePickerVisible = false)
        is PrayerTimesResult.PrayerTimesLoaded -> {
            prevState.copy(
                isLoading = false,
                prayerTimes = prevState.prayerTimes?.copy(
                    fajr = result.prayerTimes.prayerTimes.fajr,
                    sunrise = result.prayerTimes.prayerTimes.sunrise,
                    dhuhr = result.prayerTimes.prayerTimes.dhuhr,
                    asr = result.prayerTimes.prayerTimes.asr,
                    maghrib = result.prayerTimes.prayerTimes.maghrib,
                    ishaa = result.prayerTimes.prayerTimes.ishaa
                )
            )
        }
    }
}
