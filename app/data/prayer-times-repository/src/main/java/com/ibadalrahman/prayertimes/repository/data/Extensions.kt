package com.ibadalrahman.prayertimes.repository.data

import android.content.SharedPreferences
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.domain.Event
import com.ibadalrahman.prayertimes.repository.data.domain.PrayerTimes
import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.EventEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.PrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.remote.responses.DayPrayerTimesResponse
import com.ibadalrahman.prayertimes.repository.data.remote.responses.EventResponse
import com.ibadalrahman.prayertimes.repository.data.remote.responses.PrayerTimesResponse

fun DayPrayerTimesResponse.toEntity(): DayPrayerTimesEntity = DayPrayerTimesEntity(
    id = this.id,
    gregorian = this.gregorian,
    hijri = this.hijri,
    prayerTimes = this.prayerTimes.toEntity(),
    weekId = this.weekId,
    event = this.event?.toEntity()
)

fun PrayerTimesResponse.toEntity(): PrayerTimesEntity = PrayerTimesEntity(
    fajr = this.fajr,
    sunrise = this.sunrise,
    dhuhr = this.dhuhr,
    asr = this.asr,
    maghrib = this.maghrib,
    ishaa = this.ishaa
)

fun EventResponse.toEntity(): EventEntity = EventEntity(
    ar = this.ar,
    en = this.en
)

fun DayPrayerTimesEntity.toDomain(): DayPrayerTimes = DayPrayerTimes(
    id = this.id,
    gregorian = this.gregorian,
    hijri = this.hijri,
    prayerTimes = this.prayerTimes.toDomain(),
    weekId = this.weekId,
    event = this.event?.toDomain()
)

fun PrayerTimesEntity.toDomain(): PrayerTimes = PrayerTimes(
    fajr = this.fajr,
    sunrise = this.sunrise,
    dhuhr = this.dhuhr,
    asr = this.asr,
    maghrib = this.maghrib,
    ishaa = this.ishaa
)

fun EventEntity.toDomain(): Event = Event(
    en = this.en,
    ar = this.ar
)

fun SharedPreferences.getDigest(year: Int): String = this.getString("digest.$year", null) ?: ""

fun SharedPreferences.setDigest(year: Int, digest: String) {
    this.edit().putString(year.toString(), digest).apply()
}
