package com.ibadalrahman.prayertimes.presenter

import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesState
import com.ibadalrahman.prayertimes.presenter.entity.PrayerViewType
import com.ibadalrahman.prayertimes.presenter.entity.WeekHadithState
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
                    hijriDate = result.prayerTimes.hijri,
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
                weekPrayerTimes = prevState.weekPrayerTimes?.copy(
                    sat = PrayerTimesState(
                        hijriDate = result.weekPrayerTimes.sat.hijri,
                        fajr = result.weekPrayerTimes.sat.prayerTimes.fajr,
                        sunrise = result.weekPrayerTimes.sat.prayerTimes.sunrise,
                        dhuhr = result.weekPrayerTimes.sat.prayerTimes.dhuhr,
                        asr = result.weekPrayerTimes.sat.prayerTimes.asr,
                        maghrib = result.weekPrayerTimes.sat.prayerTimes.maghrib,
                        ishaa = result.weekPrayerTimes.sat.prayerTimes.ishaa
                    ),
                    sun = PrayerTimesState(
                        hijriDate = result.weekPrayerTimes.sun.hijri,
                        fajr = result.weekPrayerTimes.sun.prayerTimes.fajr,
                        sunrise = result.weekPrayerTimes.sun.prayerTimes.sunrise,
                        dhuhr = result.weekPrayerTimes.sun.prayerTimes.dhuhr,
                        asr = result.weekPrayerTimes.sun.prayerTimes.asr,
                        maghrib = result.weekPrayerTimes.sun.prayerTimes.maghrib,
                        ishaa = result.weekPrayerTimes.sun.prayerTimes.ishaa
                    ),
                    mon = PrayerTimesState(
                        hijriDate = result.weekPrayerTimes.mon.hijri,
                        fajr = result.weekPrayerTimes.mon.prayerTimes.fajr,
                        sunrise = result.weekPrayerTimes.mon.prayerTimes.sunrise,
                        dhuhr = result.weekPrayerTimes.mon.prayerTimes.dhuhr,
                        asr = result.weekPrayerTimes.mon.prayerTimes.asr,
                        maghrib = result.weekPrayerTimes.mon.prayerTimes.maghrib,
                        ishaa = result.weekPrayerTimes.mon.prayerTimes.ishaa
                    ),
                    tue = PrayerTimesState(
                        hijriDate = result.weekPrayerTimes.tue.hijri,
                        fajr = result.weekPrayerTimes.tue.prayerTimes.fajr,
                        sunrise = result.weekPrayerTimes.tue.prayerTimes.sunrise,
                        dhuhr = result.weekPrayerTimes.tue.prayerTimes.dhuhr,
                        asr = result.weekPrayerTimes.tue.prayerTimes.asr,
                        maghrib = result.weekPrayerTimes.tue.prayerTimes.maghrib,
                        ishaa = result.weekPrayerTimes.tue.prayerTimes.ishaa
                    ),
                    wed = PrayerTimesState(
                        hijriDate = result.weekPrayerTimes.wed.hijri,
                        fajr = result.weekPrayerTimes.wed.prayerTimes.fajr,
                        sunrise = result.weekPrayerTimes.wed.prayerTimes.sunrise,
                        dhuhr = result.weekPrayerTimes.wed.prayerTimes.dhuhr,
                        asr = result.weekPrayerTimes.wed.prayerTimes.asr,
                        maghrib = result.weekPrayerTimes.wed.prayerTimes.maghrib,
                        ishaa = result.weekPrayerTimes.wed.prayerTimes.ishaa
                    ),
                    thu = PrayerTimesState(
                        hijriDate = result.weekPrayerTimes.thu.hijri,
                        fajr = result.weekPrayerTimes.thu.prayerTimes.fajr,
                        sunrise = result.weekPrayerTimes.thu.prayerTimes.sunrise,
                        dhuhr = result.weekPrayerTimes.thu.prayerTimes.dhuhr,
                        asr = result.weekPrayerTimes.thu.prayerTimes.asr,
                        maghrib = result.weekPrayerTimes.thu.prayerTimes.maghrib,
                        ishaa = result.weekPrayerTimes.thu.prayerTimes.ishaa
                    ),
                    fri = PrayerTimesState(
                        hijriDate = result.weekPrayerTimes.fri.hijri,
                        fajr = result.weekPrayerTimes.fri.prayerTimes.fajr,
                        sunrise = result.weekPrayerTimes.fri.prayerTimes.sunrise,
                        dhuhr = result.weekPrayerTimes.fri.prayerTimes.dhuhr,
                        asr = result.weekPrayerTimes.fri.prayerTimes.asr,
                        maghrib = result.weekPrayerTimes.fri.prayerTimes.maghrib,
                        ishaa = result.weekPrayerTimes.fri.prayerTimes.ishaa
                    ),
                    hadithState = result.weekPrayerTimes.hadith?.let { hadith ->
                        WeekHadithState(hadith = hadith.hadith, note = hadith.note)
                    }
                )
            )
        }
    }
}
