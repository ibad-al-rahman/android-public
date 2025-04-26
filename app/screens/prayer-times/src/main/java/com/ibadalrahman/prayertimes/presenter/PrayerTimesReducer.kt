package com.ibadalrahman.prayertimes.presenter

import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import com.ibadalrahman.prayertimes.presenter.entity.PrayerViewType
import java.util.Locale

object PrayerTimesReducer {
    fun reduce(
        prevState: PrayerTimesScreenState,
        result: PrayerTimesResult
    ): PrayerTimesScreenState = when(result) {
        PrayerTimesResult.Loading -> prevState.copy(isLoading = true)
        PrayerTimesResult.ShowDatePicker -> prevState.copy(isDatePickerVisible = true)
        PrayerTimesResult.HideDatePicker -> prevState.copy(isDatePickerVisible = false)
        PrayerTimesResult.ShowDailyView -> prevState.copy(prayerViewType = PrayerViewType.DAILY)
        PrayerTimesResult.ShowWeeklyView -> prevState.copy(prayerViewType = PrayerViewType.WEEKLY)
        is PrayerTimesResult.PrayerTimesLoaded -> {
            prevState.copy(
                isLoading = false,
                date = result.prayerTimes.gregorian,
                prayerTimes = prevState.prayerTimes?.copy(
                    fajr = result.prayerTimes.prayerTimes.fajr,
                    sunrise = result.prayerTimes.prayerTimes.sunrise,
                    dhuhr = result.prayerTimes.prayerTimes.dhuhr,
                    asr = result.prayerTimes.prayerTimes.asr,
                    maghrib = result.prayerTimes.prayerTimes.maghrib,
                    ishaa = result.prayerTimes.prayerTimes.ishaa
                ),
                event = if (Locale.getDefault().language == "en") {
                    result.prayerTimes.event?.en
                } else {
                    result.prayerTimes.event?.ar
                },
                weekPrayerTimes = result.weekPrayerTimes
            )
        }
    }
}
