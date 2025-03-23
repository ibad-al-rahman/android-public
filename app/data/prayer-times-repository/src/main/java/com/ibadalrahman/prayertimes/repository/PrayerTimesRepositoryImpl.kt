package com.ibadalrahman.prayertimes.repository

import com.ibadalrahman.prayertimes.repository.data.entity.YearDailyPrayerTimesResponse
import com.ibadalrahman.prayertimes.repository.data.remote.PrayerTimesRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimesRepositoryImpl @Inject constructor(
    private val remoteDataSource: PrayerTimesRemoteDataSource
): PrayerTimesRepository {
    override suspend fun getYearDailyPrayerTimes(year: Int): Result<YearDailyPrayerTimesResponse> {
        return remoteDataSource.getYearDailyPrayerTimes(year = year)
    }
}
