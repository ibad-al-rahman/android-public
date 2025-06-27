package com.ibadalrahman.prayertimes.repository.data.local

import android.content.SharedPreferences
import com.ibadalrahman.prayertimes.repository.data.clearDigests
import com.ibadalrahman.prayertimes.repository.data.getDigest
import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.WeekEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.WeekPrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.setDigest
import javax.inject.Inject

class PrayerTimesLocalDataSourceImpl @Inject constructor(
    private val prayerTimesDao: PrayerTimesDao,
    private val sharedPreferences: SharedPreferences,
): PrayerTimesLocalDataSource {
    override fun insertDayPrayerTime(vararg prayerTimes: DayPrayerTimesEntity) {
        prayerTimesDao.insertDayPrayerTime(*prayerTimes)
    }

    override fun insertWeekPrayerTime(vararg week: WeekEntity) {
        prayerTimesDao.insertWeekPrayerTime(*week)
    }

    override fun findDayPrayerTimeById(id: Int): DayPrayerTimesEntity? =
        prayerTimesDao.findDayPrayerTimeById(id)

    override fun findWeekPrayerTimeById(id: Int): WeekPrayerTimesEntity =
        prayerTimesDao.findWeekPrayerTimeById(id)

    override fun getDigest(year: Int): String = sharedPreferences.getDigest(year = year)

    override fun setDigest(year: Int, digest: String) {
        sharedPreferences.setDigest(year = year, digest = digest)
    }

    override fun deleteAllDigests() = sharedPreferences.clearDigests()

    override fun deleteAllDayPrayerTimes() {
        prayerTimesDao.deleteAllDayPrayerTimes()
    }

    override fun deleteAllWeekPrayerTimes() {
        prayerTimesDao.deleteAllWeekPrayerTimes()
    }
}
