package org.ibadalrahman.prayertimes.repository.data.domain

import java.util.Date

data class PrayerTimes(
    val imsak: Date?,
    val fajr: Date,
    val sunrise: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val ishaa: Date
)
