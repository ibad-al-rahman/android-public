package com.ibadalrahman.prayertimes.repository.data.local

import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.WeekEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.WeekPrayerTimesEntity

interface PrayerTimesLocalDataSource {
    fun insertDayPrayerTime(vararg prayerTimes: DayPrayerTimesEntity)
    fun insertWeekPrayerTime(vararg week: WeekEntity)
    fun findDayPrayerTimeById(id: Int): DayPrayerTimesEntity
    fun findWeekPrayerTimeById(id: Int): WeekPrayerTimesEntity
    fun getDigest(year: Int): String
    fun setDigest(year: Int, digest: String)
    fun deleteAllDayPrayerTimes()
    fun deleteAllWeekPrayerTimes()
}
