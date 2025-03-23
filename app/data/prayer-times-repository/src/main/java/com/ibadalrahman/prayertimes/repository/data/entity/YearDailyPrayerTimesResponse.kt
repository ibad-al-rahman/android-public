package com.ibadalrahman.prayertimes.repository.data.entity

import com.google.gson.annotations.SerializedName

data class YearDailyPrayerTimesResponse(
    @SerializedName("year") val year: List<DayPrayerTimesResponse>,
    @SerializedName("sha1") val sha1: String
)
