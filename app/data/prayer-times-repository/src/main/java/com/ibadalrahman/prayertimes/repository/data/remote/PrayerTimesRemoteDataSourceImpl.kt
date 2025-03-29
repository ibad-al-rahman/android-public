package com.ibadalrahman.prayertimes.repository.data.remote

import com.ibadalrahman.network.extensions.apiCall
import com.ibadalrahman.prayertimes.repository.data.remote.responses.Sha1Response
import com.ibadalrahman.prayertimes.repository.data.remote.responses.YearDailyPrayerTimesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimesRemoteDataSourceImpl @Inject constructor(
    private val prayerTimesApi: PrayerTimesApi
): PrayerTimesRemoteDataSource {
    override suspend fun getYearDailyPrayerTimes(year: Int): Result<YearDailyPrayerTimesResponse> =
        prayerTimesApi.getYearDailyPrayerTimes(year = year).apiCall()

    override suspend fun getYearSha1(year: Int): Result<Sha1Response> =
        prayerTimesApi.getYearSha1(year = year).apiCall()
}
