package com.ibadalrahman.prayertimes.repository.data.local

import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity

interface PrayerTimesLocalDataSource {
    fun insertAll(vararg prayerTimes: DayPrayerTimesEntity)
    fun findById(id: Int): DayPrayerTimesEntity
    fun deleteAll()
}
