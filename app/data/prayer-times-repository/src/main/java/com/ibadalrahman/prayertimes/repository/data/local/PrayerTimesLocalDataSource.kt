package com.ibadalrahman.prayertimes.repository.data.local

import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity

interface PrayerTimesLocalDataSource {
    fun insertAll(vararg prayerTimes: DayPrayerTimesEntity)
    fun findById(id: Int): DayPrayerTimesEntity
    fun getDigest(year: Int): String
    fun setDigest(year: Int, digest: String)
    fun deleteAll()
}
