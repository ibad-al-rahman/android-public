package org.ibadalrahman.prayertimes.repository.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class WeekPrayerTimesEntity(
    @Embedded val week: WeekEntity,

    @Relation(parentColumn = "monId", entityColumn = "id")
    val mon: DayPrayerTimesEntity?,

    @Relation(parentColumn = "tueId", entityColumn = "id")
    val tue: DayPrayerTimesEntity?,

    @Relation(parentColumn = "wedId", entityColumn = "id")
    val wed: DayPrayerTimesEntity?,

    @Relation(parentColumn = "thuId", entityColumn = "id")
    val thu: DayPrayerTimesEntity?,

    @Relation(parentColumn = "friId", entityColumn = "id")
    val fri: DayPrayerTimesEntity?,

    @Relation(parentColumn = "satId", entityColumn = "id")
    val sat: DayPrayerTimesEntity?,

    @Relation(parentColumn = "sunId", entityColumn = "id")
    val sun: DayPrayerTimesEntity?,
)
