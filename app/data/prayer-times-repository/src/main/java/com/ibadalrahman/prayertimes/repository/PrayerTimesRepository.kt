package com.ibadalrahman.prayertimes.repository

import com.ibadalrahman.prayertimes.repository.data.entity.YearDailyPrayerTimesResponse

interface PrayerTimesRepository {
    suspend fun getYearDailyPrayerTimes(year: Int): Result<YearDailyPrayerTimesResponse>
}
