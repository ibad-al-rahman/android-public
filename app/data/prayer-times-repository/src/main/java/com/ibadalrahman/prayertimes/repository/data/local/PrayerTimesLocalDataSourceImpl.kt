package com.ibadalrahman.prayertimes.repository.data.local

import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity
import javax.inject.Inject

class PrayerTimesLocalDataSourceImpl@Inject constructor(
    private val prayerTimesDao: PrayerTimesDao
): PrayerTimesLocalDataSource {
    override fun insertAll(vararg prayerTimes: DayPrayerTimesEntity) {
        prayerTimesDao.insertAll(*prayerTimes)
    }

    override fun findById(id: Int): DayPrayerTimesEntity = prayerTimesDao.findById(id)

    override fun deleteAll() {
        prayerTimesDao.deleteAll()
    }
}
