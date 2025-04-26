package com.ibadalrahman.prayertimes.repository

import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.domain.WeekPrayerTimes

interface PrayerTimesRepository {
    suspend fun getDayPrayerTimes(year: Int, month: Int, day: Int): Result<DayPrayerTimes>
    suspend fun getWeekPrayerTimes(weekId: Int): Result<WeekPrayerTimes>
    suspend fun fetchPrayerTimes(year: Int): Result<Unit>
    suspend fun fetchDigest(year: Int): Result<String>
    suspend fun getDigest(year: Int): String
}
