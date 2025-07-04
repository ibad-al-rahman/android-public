package org.ibadalrahman.prayertimes.repository.data.remote.responses

import com.google.gson.annotations.SerializedName

data class WeekPrayerTimesResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("mon") val mon: DayPrayerTimesResponse?,
    @SerializedName("tue") val tue: DayPrayerTimesResponse?,
    @SerializedName("wed") val wed: DayPrayerTimesResponse?,
    @SerializedName("thu") val thu: DayPrayerTimesResponse?,
    @SerializedName("fri") val fri: DayPrayerTimesResponse?,
    @SerializedName("sat") val sat: DayPrayerTimesResponse?,
    @SerializedName("sun") val sun: DayPrayerTimesResponse?,
    @SerializedName("hadith") val hadith: WeekHadithResponse?
)
