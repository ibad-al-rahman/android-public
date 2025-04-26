package com.ibadalrahman.prayertimes.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.ibadalrahman.prayertimes.repository.data.domain.WeekPrayerTimes
import java.util.Date

@Stable
@Immutable
data class PrayerTimesScreenState(
    val isLoading: Boolean,
    val date: Date,
    val isDatePickerVisible: Boolean,
    val prayerViewType: PrayerViewType,
    val prayerTimes: PrayerTimesState?,
    val weekPrayerTimes: WeekPrayerTimes?,
    val event: String?,
) {
    companion object {
        val initialState: PrayerTimesScreenState
            get() = PrayerTimesScreenState(
                isLoading = false,
                date = Date(),
                isDatePickerVisible = false,
                prayerViewType = PrayerViewType.DAILY,
                prayerTimes = PrayerTimesState(
                    fajr = Date(),
                    sunrise = Date(),
                    dhuhr = Date(),
                    asr = Date(),
                    maghrib = Date(),
                    ishaa = Date()
                ),
                weekPrayerTimes = null,
                event = null
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

@Stable
@Immutable
enum class PrayerViewType {
    DAILY,
    WEEKLY
}
