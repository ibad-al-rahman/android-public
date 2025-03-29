package com.ibadalrahman.prayertimes.repository.data.domain

data class PrayerTimes(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val ishaa: String
)
