package com.ibadalrahman.prayertimes.repository.data.domain

import java.util.Date

data class PrayerTimes(
    val fajr: Date,
    val sunrise: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val ishaa: Date
)
