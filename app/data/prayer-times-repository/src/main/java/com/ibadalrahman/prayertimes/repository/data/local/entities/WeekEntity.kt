package com.ibadalrahman.prayertimes.repository.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "week_prayer_times")
data class WeekEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val monId: Int?,
    val tueId: Int?,
    val wedId: Int?,
    val thuId: Int?,
    val friId: Int?,
    val satId: Int?,
    val sunId: Int?,
    @Embedded
    val hadith: WeekHadithEntity?
)
