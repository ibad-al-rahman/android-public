package com.ibadalrahman.prayertimes.repository.data.entity

import com.google.gson.annotations.SerializedName

data class PrayerTimesResponse(
    @SerializedName("fajr") val fajr: String,
    @SerializedName("sunrise") val sunrise: String,
    @SerializedName("dhuhr") val dhuhr: String,
    @SerializedName("asr") val asr: String,
    @SerializedName("maghrib") val maghrib: String,
    @SerializedName("ishaa") val ishaa: String
)
