package com.ibadalrahman.prayertimes.repository.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity

@Dao
interface PrayerTimesDao {
    @Insert
    fun insertAll(vararg prayerTimes: DayPrayerTimesEntity)

    @Query("SELECT * FROM day_prayer_times WHERE id = (:id)")
    fun findById(id: Int): DayPrayerTimesEntity

    @Query("DELETE FROM day_prayer_times")
    fun deleteAll()
}
