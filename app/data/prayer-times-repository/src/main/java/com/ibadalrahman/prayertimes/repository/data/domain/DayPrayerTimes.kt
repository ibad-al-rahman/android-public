package com.ibadalrahman.prayertimes.repository.data.domain

import java.util.Date

data class DayPrayerTimes(
    val id: Int,
    val gregorian: Date,
    val hijri: String,
    val prayerTimes: PrayerTimes,
    val weekId: Int,
    val event: Event?
)
