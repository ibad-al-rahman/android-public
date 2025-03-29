package com.ibadalrahman.prayertimes.repository.data.remote

import com.ibadalrahman.prayertimes.repository.data.remote.responses.Sha1Response
import com.ibadalrahman.prayertimes.repository.data.remote.responses.YearDailyPrayerTimesResponse

interface PrayerTimesRemoteDataSource {
    suspend fun getYearDailyPrayerTimes(year: Int): Result<YearDailyPrayerTimesResponse>
    suspend fun getYearSha1(year: Int): Result<Sha1Response>
}
