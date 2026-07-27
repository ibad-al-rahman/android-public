package org.ibadalrahman.prayertimes.presenter

import org.ibadalrahman.miqat.repository.data.domain.MiqatData
import org.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesState
import org.ibadalrahman.prayertimes.presenter.entity.PrayerViewType

object PrayerTimesReducer {
    fun reduce(
        prevState: PrayerTimesScreenState,
        result: PrayerTimesResult
    ): PrayerTimesScreenState = when(result) {
        PrayerTimesResult.ShowDatePicker -> prevState.copy(isDatePickerVisible = true)
        PrayerTimesResult.HideDatePicker -> prevState.copy(isDatePickerVisible = false)
        PrayerTimesResult.ShowDailyView -> prevState.copy(prayerViewType = PrayerViewType.DAILY)
        PrayerTimesResult.ShowWeeklyView -> prevState.copy(prayerViewType = PrayerViewType.WEEKLY)
        is PrayerTimesResult.PrayerTimesLoaded -> {
            prevState.copy(
                date = result.day.gregorian,
                prayerTimes = result.day.toPrayerTimesState(hijri = result.dayHijri),
                event = result.event,
                weekPrayerTimes = prevState.weekPrayerTimes?.copy(
                    sat = result.week.getOrNull(0)?.toPrayerTimesState(),
                    sun = result.week.getOrNull(1)?.toPrayerTimesState(),
                    mon = result.week.getOrNull(2)?.toPrayerTimesState(),
                    tue = result.week.getOrNull(3)?.toPrayerTimesState(),
                    wed = result.week.getOrNull(4)?.toPrayerTimesState(),
                    thu = result.week.getOrNull(5)?.toPrayerTimesState(),
                    fri = result.week.getOrNull(6)?.toPrayerTimesState(),
                )
            )
        }
        is PrayerTimesResult.ShareTextProcessed -> prevState
    }

    private fun MiqatData.toPrayerTimesState(hijri: String = ""): PrayerTimesState =
        PrayerTimesState(
            hijriDate = hijri,
            fajr = fajr,
            sunrise = sunrise,
            dhuhr = dhuhr,
            asr = asr,
            maghrib = maghrib,
            ishaa = ishaa,
        )
}
