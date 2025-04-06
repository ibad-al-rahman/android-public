package com.ibadalrahman.prayertimes.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Date

@Stable
@Immutable
data class PrayerTimesScreenState(
    val isLoading: Boolean,
    val date: Date,
    val prayerTimes: PrayerTimesState
) {
    companion object {
        val initialState: PrayerTimesScreenState
            get() = PrayerTimesScreenState(
                isLoading = false,
                date = Date(),
                prayerTimes = PrayerTimesState(
                    fajr = "",
                    sunrise = "",
                    dhuhr = "",
                    asr = "",
                    maghrib = "",
                    ishaa = ""
                )
            )
    }
}

@Stable
@Immutable
data class PrayerTimesState(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val ishaa: String
)
