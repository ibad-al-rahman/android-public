package com.ibadalrahman.prayertimes.repository.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_prayer_times")
data class DayPrayerTimesEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val gregorian: String,

    val hijri: String,

    @Embedded
    val prayerTimes: PrayerTimesEntity,

    val weekId: Int,

    @Embedded
    val event: EventEntity?
)
