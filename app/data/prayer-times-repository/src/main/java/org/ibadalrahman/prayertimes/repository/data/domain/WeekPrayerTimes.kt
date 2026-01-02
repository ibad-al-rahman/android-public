package org.ibadalrahman.prayertimes.repository.data.domain

data class WeekPrayerTimes(
    val id: Int,
    val mon: DayPrayerTimes? = null,
    val tue: DayPrayerTimes? = null,
    val wed: DayPrayerTimes? = null,
    val thu: DayPrayerTimes? = null,
    val fri: DayPrayerTimes? = null,
    val sat: DayPrayerTimes? = null,
    val sun: DayPrayerTimes? = null,
    val hadith: WeekHadith? = null,
)
