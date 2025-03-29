package com.ibadalrahman.prayertimes.repository.data.local

import android.content.SharedPreferences
import com.ibadalrahman.prayertimes.repository.data.getDigest
import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.setDigest
import javax.inject.Inject

class PrayerTimesLocalDataSourceImpl@Inject constructor(
    private val prayerTimesDao: PrayerTimesDao,
    private val sharedPreferences: SharedPreferences,
): PrayerTimesLocalDataSource {
    override fun insertAll(vararg prayerTimes: DayPrayerTimesEntity) {
        prayerTimesDao.insertAll(*prayerTimes)
    }

    override fun findById(id: Int): DayPrayerTimesEntity = prayerTimesDao.findById(id)

    override fun getDigest(year: Int): String = sharedPreferences.getDigest(year = year)

    override fun setDigest(year: Int, digest: String) {
        sharedPreferences.setDigest(year = year, digest = digest)
    }

    override fun deleteAll() {
        prayerTimesDao.deleteAll()
    }
}
