package com.ibadalrahman.prayertimes.repository.data.remote

import com.ibadalrahman.network.extensions.apiCall
import com.ibadalrahman.prayertimes.repository.data.entity.YearDailyPrayerTimesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimesRemoteDataSourceImpl @Inject constructor(
    private val prayerTimesApi: PrayerTimesApi
): PrayerTimesRemoteDataSource {
    override suspend fun getYearDailyPrayerTimes(year: Int): Result<YearDailyPrayerTimesResponse> =
        prayerTimesApi.getYearDailyPrayerTimes(year = year).apiCall()
}
