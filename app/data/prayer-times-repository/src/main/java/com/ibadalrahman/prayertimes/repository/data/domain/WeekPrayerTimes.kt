package com.ibadalrahman.prayertimes.repository.data.domain

data class WeekPrayerTimes(
    val id: Int,
    val mon: DayPrayerTimes,
    val tue: DayPrayerTimes,
    val wed: DayPrayerTimes,
    val thu: DayPrayerTimes,
    val fri: DayPrayerTimes,
    val sat: DayPrayerTimes,
    val sun: DayPrayerTimes,
    val hadith: WeekHadith? = null,
)
