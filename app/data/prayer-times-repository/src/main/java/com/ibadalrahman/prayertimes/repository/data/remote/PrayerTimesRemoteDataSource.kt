package com.ibadalrahman.prayertimes.repository.data.remote

import com.ibadalrahman.prayertimes.repository.data.entity.YearDailyPrayerTimesResponse

interface PrayerTimesRemoteDataSource {
    suspend fun getYearDailyPrayerTimes(year: Int): Result<YearDailyPrayerTimesResponse>
}
