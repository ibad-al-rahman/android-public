package org.ibadalrahman.prayertimes.repository.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import org.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity
import org.ibadalrahman.prayertimes.repository.data.local.entities.WeekEntity
import org.ibadalrahman.prayertimes.repository.data.local.entities.WeekPrayerTimesEntity

@Dao
interface PrayerTimesDao {
    @Insert
    fun insertDayPrayerTime(vararg prayerTimes: DayPrayerTimesEntity)

    @Insert
    fun insertWeekPrayerTime(vararg week: WeekEntity)

    @Query("SELECT * FROM day_prayer_times WHERE id = (:id)")
    fun findDayPrayerTimeById(id: Int): DayPrayerTimesEntity?

    @Transaction
    @Query("SELECT * FROM week_prayer_times WHERE id = (:id)")
    fun findWeekPrayerTimeById(id: Int): WeekPrayerTimesEntity?

    @Query("DELETE FROM day_prayer_times")
    fun deleteAllDayPrayerTimes()

    @Query("DELETE FROM week_prayer_times")
    fun deleteAllWeekPrayerTimes()
}
