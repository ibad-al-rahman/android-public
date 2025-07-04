package org.ibadalrahman.prayertimes.repository.data.remote

import org.ibadalrahman.network.extensions.apiCall
import org.ibadalrahman.prayertimes.repository.data.remote.responses.Sha1Response
import org.ibadalrahman.prayertimes.repository.data.remote.responses.YearDailyPrayerTimesResponse
import org.ibadalrahman.prayertimes.repository.data.remote.responses.YearWeeklyPrayerTimesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimesRemoteDataSourceImpl @Inject constructor(
    private val prayerTimesApi: PrayerTimesApi
): PrayerTimesRemoteDataSource {
    override suspend fun getYearDailyPrayerTimes(year: Int): Result<YearDailyPrayerTimesResponse> =
        prayerTimesApi.getYearDailyPrayerTimes(year = year).apiCall()

    override suspend fun getYearWeeklyPrayerTimes(year: Int): Result<YearWeeklyPrayerTimesResponse> =
        prayerTimesApi.getYearWeeklyPrayerTimes(year = year).apiCall()

    override suspend fun getYearSha1(year: Int): Result<Sha1Response> =
        prayerTimesApi.getYearSha1(year = year).apiCall()
}
