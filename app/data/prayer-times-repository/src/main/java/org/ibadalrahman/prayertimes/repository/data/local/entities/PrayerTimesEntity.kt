package org.ibadalrahman.prayertimes.repository.data.local.entities

data class PrayerTimesEntity(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val ishaa: String
)
