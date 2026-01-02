package org.ibadalrahman.prayertimes.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import org.ibadalrahman.prayertimes.repository.data.domain.WeekPrayerTimes
import java.util.Date

@Stable
@Immutable
data class PrayerTimesScreenState(
    val isLoading: Boolean,
    val date: Date,
    val isDatePickerVisible: Boolean,
    val prayerViewType: PrayerViewType,
    val hasError: Boolean,
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
                hasError = false,
                prayerTimes = PrayerTimesState(
                    hijriDate = "",
                    fajr = Date(),
                    sunrise = Date(),
                    dhuhr = Date(),
                    asr = Date(),
                    maghrib = Date(),
                    ishaa = Date()
                ),
                weekPrayerTimes = WeekPrayerTimesState(),
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
    val sat: PrayerTimesState? = null,
    val sun: PrayerTimesState? = null,
    val mon: PrayerTimesState? = null,
    val tue: PrayerTimesState? = null,
    val wed: PrayerTimesState? = null,
    val thu: PrayerTimesState? = null,
    val fri: PrayerTimesState? = null,
    val hadithState: WeekHadithState? = null
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
