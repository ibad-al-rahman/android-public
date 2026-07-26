package org.ibadalrahman.miqat.repository.data.domain

import org.ibadalrahman.miqat.HijriDateInfo
import org.ibadalrahman.miqat.IslamicEvent
import org.ibadalrahman.miqat.repository.data.prayerTimes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Prayer times, Hijri date, and Islamic events for a single day. Port of iOS `MiqatData`.
 *
 * [imsak] is populated only during Ramadan (fajr − 20 min) and [eid] only on Eid
 * (sunrise + 45 min), matching the iOS derived rules.
 */
data class MiqatData(
    /** yyyyMMdd, e.g. "20260726". */
    val id: String,
    val gregorian: Date,
    val imsak: Date?,
    val fajr: Date,
    val sunrise: Date,
    val eid: Date?,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val ishaa: Date,
    val hijriDate: MiqatHijriDate,
    val islamicEvents: List<IslamicEvent>,
) {
    companion object {
        private const val RAMADAN_MONTH = 9
        private const val IMSAK_OFFSET_MS = 20 * 60 * 1000L
        private const val EID_OFFSET_MS = 45 * 60 * 1000L

        /**
         * Computes a [MiqatData] for [timestampSecs] using [method].
         *
         * Both library handles ([org.ibadalrahman.miqat.PrayerTimes] and [HijriDateInfo]) are
         * `AutoCloseable`; they are read and closed here so callers never touch the native handles.
         */
        fun compute(timestampSecs: Long, method: MiqatCalculationMethod): MiqatData {
            val gregorian = Date(timestampSecs * 1000)
            val id = SimpleDateFormat("yyyyMMdd", Locale("en")).format(gregorian)

            val times = method.prayerTimes(timestampSecs).use { prayerTimes ->
                DayTimes(
                    fajr = Date(prayerTimes.fajr() * 1000),
                    sunrise = Date(prayerTimes.sunrise() * 1000),
                    dhuhr = Date(prayerTimes.dhuhr() * 1000),
                    asr = Date(prayerTimes.asr() * 1000),
                    maghrib = Date(prayerTimes.maghrib() * 1000),
                    ishaa = Date(prayerTimes.ishaa() * 1000),
                )
            }

            val (hijriDate, islamicEvents) =
                HijriDateInfo.fromTimestamp(timestampSecs).use { info ->
                    MiqatHijriDate.from(info.date()) to info.events()
                }

            val fajr = times.fajr
            val sunrise = times.sunrise

            val imsak = if (hijriDate.month == RAMADAN_MONTH) {
                Date(fajr.time - IMSAK_OFFSET_MS)
            } else {
                null
            }

            val eid = if (islamicEvents.any { it == IslamicEvent.EID_AL_ADHA || it == IslamicEvent.EID_AL_FITR }) {
                Date(sunrise.time + EID_OFFSET_MS)
            } else {
                null
            }

            return MiqatData(
                id = id,
                gregorian = gregorian,
                imsak = imsak,
                fajr = fajr,
                sunrise = sunrise,
                eid = eid,
                dhuhr = times.dhuhr,
                asr = times.asr,
                maghrib = times.maghrib,
                ishaa = times.ishaa,
                hijriDate = hijriDate,
                islamicEvents = islamicEvents,
            )
        }

        private data class DayTimes(
            val fajr: Date,
            val sunrise: Date,
            val dhuhr: Date,
            val asr: Date,
            val maghrib: Date,
            val ishaa: Date,
        )
    }
}
