package org.ibadalrahman.prayertimes.presenter

import org.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesState
import org.ibadalrahman.prayertimes.presenter.entity.PrayerViewType
import org.ibadalrahman.prayertimes.presenter.entity.WeekHadithState
import java.util.Locale

object PrayerTimesReducer {
    fun reduce(
        prevState: PrayerTimesScreenState,
        result: PrayerTimesResult
    ): PrayerTimesScreenState = when(result) {
        PrayerTimesResult.Loading -> prevState.copy(
            isLoading = true,
            hasError = false
        )
        PrayerTimesResult.UnknownError -> prevState.copy(
            isLoading = false,
            hasError = true
        )
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
                    sat = result.weekPrayerTimes.sat?.let { sat ->
                        PrayerTimesState(
                            hijriDate = sat.hijri,
                            fajr = sat.prayerTimes.fajr,
                            sunrise = sat.prayerTimes.sunrise,
                            dhuhr = sat.prayerTimes.dhuhr,
                            asr = sat.prayerTimes.asr,
                            maghrib = sat.prayerTimes.maghrib,
                            ishaa = sat.prayerTimes.ishaa
                        )
                    },
                    sun = result.weekPrayerTimes.sun?.let { sun ->
                        PrayerTimesState(
                            hijriDate = sun.hijri,
                            fajr = sun.prayerTimes.fajr,
                            sunrise = sun.prayerTimes.sunrise,
                            dhuhr = sun.prayerTimes.dhuhr,
                            asr = sun.prayerTimes.asr,
                            maghrib = sun.prayerTimes.maghrib,
                            ishaa = sun.prayerTimes.ishaa
                        )
                    },
                    mon = result.weekPrayerTimes.mon?.let { mon ->
                        PrayerTimesState(
                            hijriDate = mon.hijri,
                            fajr = mon.prayerTimes.fajr,
                            sunrise = mon.prayerTimes.sunrise,
                            dhuhr = mon.prayerTimes.dhuhr,
                            asr = mon.prayerTimes.asr,
                            maghrib = mon.prayerTimes.maghrib,
                            ishaa = mon.prayerTimes.ishaa
                        )
                    },
                    tue = result.weekPrayerTimes.tue?.let { tue ->
                        PrayerTimesState(
                            hijriDate = tue.hijri,
                            fajr = tue.prayerTimes.fajr,
                            sunrise = tue.prayerTimes.sunrise,
                            dhuhr = tue.prayerTimes.dhuhr,
                            asr = tue.prayerTimes.asr,
                            maghrib = tue.prayerTimes.maghrib,
                            ishaa = tue.prayerTimes.ishaa
                        )
                    },
                    wed = result.weekPrayerTimes.wed?.let { wed ->
                        PrayerTimesState(
                            hijriDate = wed.hijri,
                            fajr = wed.prayerTimes.fajr,
                            sunrise = wed.prayerTimes.sunrise,
                            dhuhr = wed.prayerTimes.dhuhr,
                            asr = wed.prayerTimes.asr,
                            maghrib = wed.prayerTimes.maghrib,
                            ishaa = wed.prayerTimes.ishaa
                        )
                    },
                    thu = result.weekPrayerTimes.thu?.let { thu ->
                        PrayerTimesState(
                            hijriDate = thu.hijri,
                            fajr = thu.prayerTimes.fajr,
                            sunrise = thu.prayerTimes.sunrise,
                            dhuhr = thu.prayerTimes.dhuhr,
                            asr = thu.prayerTimes.asr,
                            maghrib = thu.prayerTimes.maghrib,
                            ishaa = thu.prayerTimes.ishaa
                        )
                    },
                    fri = result.weekPrayerTimes.fri?.let { fri ->
                        PrayerTimesState(
                            hijriDate = fri.hijri,
                            fajr = fri.prayerTimes.fajr,
                            sunrise = fri.prayerTimes.sunrise,
                            dhuhr = fri.prayerTimes.dhuhr,
                            asr = fri.prayerTimes.asr,
                            maghrib = fri.prayerTimes.maghrib,
                            ishaa = fri.prayerTimes.ishaa
                        )
                    },
                    hadithState = result.weekPrayerTimes.hadith?.let { hadith ->
                        WeekHadithState(hadith = hadith.hadith, note = hadith.note)
                    }
                )
            )
        }
        is PrayerTimesResult.ShareTextProcessed -> prevState
    }
}
