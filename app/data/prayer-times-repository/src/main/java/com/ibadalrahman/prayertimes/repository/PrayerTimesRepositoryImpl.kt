package com.ibadalrahman.prayertimes.repository

import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.local.PrayerTimesLocalDataSource
import com.ibadalrahman.prayertimes.repository.data.remote.PrayerTimesRemoteDataSource
import com.ibadalrahman.prayertimes.repository.data.remote.responses.DayPrayerTimesResponse
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
        return Result.success(localDatasource.findById(id = id).toDomain())
    }

    override suspend fun fetchPrayerTimes(year: Int): Result<Unit> {
        remoteDataSource
            .getYearDailyPrayerTimes(year = year)
            .onSuccess {
                localDatasource.deleteAll()
                localDatasource.insertAll(
                    *it.year.map(DayPrayerTimesResponse::toEntity).toTypedArray()
                )
                localDatasource.setDigest(year = year, digest = it.sha1)
            }
            .onFailure {
                return Result.failure(it)
            }
        return Result.success(Unit)
    }

    override suspend fun fetchDigest(year: Int): Result<String> =
        remoteDataSource.getYearSha1(year = year).map { it.sha1 }

    override suspend fun getDigest(year: Int): String = localDatasource.getDigest(year = year)
}
