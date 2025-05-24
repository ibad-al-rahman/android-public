package com.ibadalrahman.widgets.prayertimes

import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class PrayerTimesWidgetViewModel @Inject constructor(
    private val prayerTimesRepository: PrayerTimesRepository
) {
    suspend fun getPrayerTimes(): Result<DayPrayerTimes> {
        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return  prayerTimesRepository.getDayPrayerTimes(year, month, day)
    }
}
