package org.ibadalrahman.prayertimes.repository.data.remote

import org.ibadalrahman.prayertimes.repository.data.remote.responses.Sha1Response
import org.ibadalrahman.prayertimes.repository.data.remote.responses.YearDailyPrayerTimesResponse
import org.ibadalrahman.prayertimes.repository.data.remote.responses.YearWeeklyPrayerTimesResponse

interface PrayerTimesRemoteDataSource {
    suspend fun getYearDailyPrayerTimes(year: Int): Result<YearDailyPrayerTimesResponse>
    suspend fun getYearWeeklyPrayerTimes(year: Int): Result<YearWeeklyPrayerTimesResponse>
    suspend fun getYearSha1(year: Int): Result<Sha1Response>
}
