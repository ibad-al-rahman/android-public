package com.ibadalrahman.prayertimes.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
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
    val weekPrayerTimes: WeekPrayerTimesState?,
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
                    hijriDate = "",
                    fajr = Date(),
                    sunrise = Date(),
                    dhuhr = Date(),
                    asr = Date(),
                    maghrib = Date(),
                    ishaa = Date()
                ),
                weekPrayerTimes = WeekPrayerTimesState(
                    sat = PrayerTimesState(
                        hijriDate = "",
                        fajr = Date(),
                        sunrise = Date(),
                        dhuhr = Date(),
                        asr = Date(),
                        maghrib = Date(),
                        ishaa = Date()
                    ),
                    sun = PrayerTimesState(
                        hijriDate = "",
                        fajr = Date(),
                        sunrise = Date(),
                        dhuhr = Date(),
                        asr = Date(),
                        maghrib = Date(),
                        ishaa = Date()
                    ),
                    mon = PrayerTimesState(
                        hijriDate = "",
                        fajr = Date(),
                        sunrise = Date(),
                        dhuhr = Date(),
                        asr = Date(),
                        maghrib = Date(),
                        ishaa = Date()
                    ),
                    tue = PrayerTimesState(
                        hijriDate = "",
                        fajr = Date(),
                        sunrise = Date(),
                        dhuhr = Date(),
                        asr = Date(),
                        maghrib = Date(),
                        ishaa = Date()
                    ),
                    wed = PrayerTimesState(
                        hijriDate = "",
                        fajr = Date(),
                        sunrise = Date(),
                        dhuhr = Date(),
                        asr = Date(),
                        maghrib = Date(),
                        ishaa = Date()
                    ),
                    thu = PrayerTimesState(
                        hijriDate = "",
                        fajr = Date(),
                        sunrise = Date(),
                        dhuhr = Date(),
                        asr = Date(),
                        maghrib = Date(),
                        ishaa = Date()
                    ),
                    fri = PrayerTimesState(
                        hijriDate = "",
                        fajr = Date(),
                        sunrise = Date(),
                        dhuhr = Date(),
                        asr = Date(),
                        maghrib = Date(),
                        ishaa = Date()
                    ),
                    hadithState = null
                ),
                event = null
            )
    }

    val compactedWeeks: List<PrayerTimesState>
        get() = listOf(
            this.weekPrayerTimes?.sat,
            this.weekPrayerTimes?.sun,
            this.weekPrayerTimes?.mon,
            this.weekPrayerTimes?.tue,
            this.weekPrayerTimes?.wed,
            this.weekPrayerTimes?.thu,
            this.weekPrayerTimes?.fri
        ).mapNotNull { it }
}

@Stable
@Immutable
data class PrayerTimesState(
    val hijriDate: String,
    val fajr: Date,
    val sunrise: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val ishaa: Date
)

@Stable
@Immutable
data class WeekPrayerTimesState(
    val sat: PrayerTimesState,
    val sun: PrayerTimesState,
    val mon: PrayerTimesState,
    val tue: PrayerTimesState,
    val wed: PrayerTimesState,
    val thu: PrayerTimesState,
    val fri: PrayerTimesState,
    val hadithState: WeekHadithState?
)

@Stable
@Immutable
data class WeekHadithState(
    val hadith: String,
    val note: String?
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
enum class WeekDay {
    SAT,
    SUN,
    MON,
    TUE,
    WED,
    THU,
    FRI
}

@Stable
@Immutable
enum class PrayerViewType {
    DAILY,
    WEEKLY
}
