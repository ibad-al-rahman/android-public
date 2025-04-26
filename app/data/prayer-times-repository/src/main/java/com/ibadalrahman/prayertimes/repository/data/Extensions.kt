package com.ibadalrahman.prayertimes.repository.data

import android.content.SharedPreferences
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.domain.Event
import com.ibadalrahman.prayertimes.repository.data.domain.PrayerTimes
import com.ibadalrahman.prayertimes.repository.data.domain.WeekHadith
import com.ibadalrahman.prayertimes.repository.data.domain.WeekPrayerTimes
import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.EventEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.PrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.WeekEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.WeekHadithEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.WeekPrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.remote.responses.DayPrayerTimesResponse
import com.ibadalrahman.prayertimes.repository.data.remote.responses.EventResponse
import com.ibadalrahman.prayertimes.repository.data.remote.responses.PrayerTimesResponse
import com.ibadalrahman.prayertimes.repository.data.remote.responses.WeekHadithResponse
import com.ibadalrahman.prayertimes.repository.data.remote.responses.WeekPrayerTimesResponse
import org.ibadalrahman.fp.safeLet
import java.text.SimpleDateFormat
import java.util.Locale

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

fun WeekPrayerTimesResponse.toEntity(): WeekEntity = WeekEntity(
    id = this.id,
    monId = this.mon?.id,
    tueId = this.tue?.id,
    wedId = this.wed?.id,
    thuId = this.thu?.id,
    friId = this.fri?.id,
    satId = this.sat?.id,
    sunId = this.sun?.id,
    hadith = this.hadith?.toEntity()
)

fun WeekHadithResponse.toEntity(): WeekHadithEntity = WeekHadithEntity(
    hadith = this.hadith,
    note = this.note
)

fun DayPrayerTimesEntity.toDomain(): DayPrayerTimes? = safeLet(
    SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(this.gregorian),
    this.prayerTimes.toDomain(this.gregorian),
) { gregorian, prayerTimes ->
    return DayPrayerTimes(
        id = this.id,
        gregorian = gregorian,
        hijri = this.hijri,
        prayerTimes = prayerTimes,
        weekId = this.weekId,
        event = event?.toDomain()
    )
}

fun PrayerTimesEntity.toDomain(date: String): PrayerTimes? {
    val format = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.ENGLISH)
    return safeLet(
        format.parse("$date ${this.fajr}"),
        format.parse("$date ${this.sunrise}"),
        format.parse("$date ${this.dhuhr}"),
        format.parse("$date ${this.asr}"),
        format.parse("$date ${this.maghrib}"),
        format.parse("$date ${this.ishaa}")
    ) { fajr, sunrise, dhuhr, asr, maghrib, ishaa ->
        return PrayerTimes(
            fajr = fajr,
            sunrise = sunrise,
            dhuhr = dhuhr,
            asr = asr,
            maghrib = maghrib,
            ishaa = ishaa
        )
    }
}

fun EventEntity.toDomain(): Event = Event(
    en = this.en,
    ar = this.ar
)

fun WeekPrayerTimesEntity.toDomain(): WeekPrayerTimes? = safeLet(
    this.mon?.toDomain(),
    this.tue?.toDomain(),
    this.wed?.toDomain(),
    this.thu?.toDomain(),
    this.fri?.toDomain(),
    this.sat?.toDomain(),
    this.sun?.toDomain()
) { mon, tue, wed, thu, fri, sat, sun ->
    return WeekPrayerTimes(
        id = this.week.id,
        mon = mon,
        tue = tue,
        wed = wed,
        thu = thu,
        fri = fri,
        sat = sat,
        sun = sun,
        hadith = this.week.hadith?.toDomain()
    )
}

fun WeekHadithEntity.toDomain(): WeekHadith = WeekHadith(
    hadith = this.hadith,
    note = this.note
)

fun SharedPreferences.getDigest(year: Int): String = this.getString("digest.$year", null) ?: ""

fun SharedPreferences.setDigest(year: Int, digest: String) {
    this.edit().putString(year.toString(), digest).apply()
}
