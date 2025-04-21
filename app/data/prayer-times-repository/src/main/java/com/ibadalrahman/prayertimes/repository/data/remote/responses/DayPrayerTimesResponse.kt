package com.ibadalrahman.prayertimes.repository.data.remote.responses

import com.google.gson.annotations.SerializedName

data class DayPrayerTimesResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("gregorian") val gregorian: String,
    @SerializedName("hijri") val hijri: String,
    @SerializedName("prayerTimes") val prayerTimes: PrayerTimesResponse,
    @SerializedName("weekId") val weekId: Int,
    @SerializedName("event") val event: EventResponse?
)
