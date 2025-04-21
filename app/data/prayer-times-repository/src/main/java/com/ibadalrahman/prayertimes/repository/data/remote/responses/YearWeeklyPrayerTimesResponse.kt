package com.ibadalrahman.prayertimes.repository.data.remote.responses

import com.google.gson.annotations.SerializedName

data class YearWeeklyPrayerTimesResponse(
    @SerializedName("weeks") val year: List<DayPrayerTimesResponse>,
    @SerializedName("sha1") val sha1: String
)
