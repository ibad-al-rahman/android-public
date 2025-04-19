package com.ibadalrahman.prayertimes.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Date

@Stable
@Immutable
data class PrayerTimesScreenState(
    val isLoading: Boolean,
    val date: Date,
    val isDatePickerVisible: Boolean,
    val prayerTimes: PrayerTimesState?
) {
    companion object {
        val initialState: PrayerTimesScreenState
            get() = PrayerTimesScreenState(
                isLoading = false,
                date = Date(),
                isDatePickerVisible = false,
                prayerTimes = PrayerTimesState(
                    fajr = Date(),
                    sunrise = Date(),
                    dhuhr = Date(),
                    asr = Date(),
                    maghrib = Date(),
                    ishaa = Date()
                )
            )
    }
}

@Stable
@Immutable
data class PrayerTimesState(
    val fajr: Date,
    val sunrise: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val ishaa: Date
)

@Stable
@Immutable
enum class Prayer {
    FAJR,
    SUNRISE,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHAA
}
