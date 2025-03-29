package com.ibadalrahman.prayertimes.repository.data.domain

data class DayPrayerTimes(
    val id: Int,
    val gregorian: String,
    val hijri: String,
    val prayerTimes: PrayerTimes,
    val weekId: String,
    val event: Event?
)
