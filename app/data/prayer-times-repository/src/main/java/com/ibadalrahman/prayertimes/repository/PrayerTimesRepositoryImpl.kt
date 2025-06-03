package com.ibadalrahman.prayertimes.repository

import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.domain.WeekPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.local.PrayerTimesLocalDataSource
import com.ibadalrahman.prayertimes.repository.data.remote.PrayerTimesRemoteDataSource
import com.ibadalrahman.prayertimes.repository.data.remote.responses.DayPrayerTimesResponse
import com.ibadalrahman.prayertimes.repository.data.remote.responses.WeekPrayerTimesResponse
import com.ibadalrahman.prayertimes.repository.data.toDomain
import com.ibadalrahman.prayertimes.repository.data.toEntity
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimesRepositoryImpl @Inject constructor(
    private val remoteDataSource: PrayerTimesRemoteDataSource,
    private val localDatasource: PrayerTimesLocalDataSource
): PrayerTimesRepository {
    override suspend fun getDayPrayerTimes(
        year: Int,
        month: Int,
        day: Int
    ): Result<DayPrayerTimes> {
        val id = String.format(
            locale = Locale("en"), format = "%04d%02d%02d", year, month, day
        ).toInt()
        val prayerTimes = localDatasource.findDayPrayerTimeById(id = id).toDomain()
            ?: return Result.failure(IllegalArgumentException("No data found for the given date"))
        return Result.success(prayerTimes)
    }

    override suspend fun getWeekPrayerTimes(weekId: Int): Result<WeekPrayerTimes> {
        val weekPrayerTimes = localDatasource.findWeekPrayerTimeById(id = weekId).toDomain()
            ?: return Result.failure(IllegalArgumentException("No data found for the given week"))
        return Result.success(weekPrayerTimes)
    }

    override suspend fun fetchPrayerTimes(year: Int): Result<Unit> {
        val dailyPrayerTimes = remoteDataSource
            .getYearDailyPrayerTimes(year = year)
            .getOrElse { return Result.failure(it) }

        val weekPrayerTimes = remoteDataSource
            .getYearWeeklyPrayerTimes(year = year)
            .getOrElse { return Result.failure(it) }

        localDatasource.deleteAllDayPrayerTimes()
        localDatasource.deleteAllWeekPrayerTimes()

        localDatasource.insertDayPrayerTime(
            *dailyPrayerTimes.year.map(DayPrayerTimesResponse::toEntity).toTypedArray()
        )
        localDatasource.insertWeekPrayerTime(
            *weekPrayerTimes.year.map(WeekPrayerTimesResponse::toEntity).toTypedArray()
        )

        localDatasource.setDigest(year = year, digest = dailyPrayerTimes.sha1)

        return Result.success(Unit)
    }

    override suspend fun fetchDigest(year: Int): Result<String> =
        remoteDataSource.getYearSha1(year = year).map { it.sha1 }

    override suspend fun getDigest(year: Int): String =
        localDatasource.getDigest(year = year)

    override suspend fun clear() {
        localDatasource.deleteAllDayPrayerTimes()
        localDatasource.deleteAllWeekPrayerTimes()
        localDatasource.deleteAllDigests()
    }
}
