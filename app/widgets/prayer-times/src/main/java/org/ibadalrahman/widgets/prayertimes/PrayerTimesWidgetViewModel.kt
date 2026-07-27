package org.ibadalrahman.widgets.prayertimes

import org.ibadalrahman.miqat.repository.MiqatRepository
import org.ibadalrahman.miqat.repository.data.domain.MiqatData
import org.ibadalrahman.resources.R
import java.text.DateFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PrayerTimesWidgetViewModel @Inject constructor(
    private val miqatRepository: MiqatRepository
) {
    suspend fun getPrayerTimes(): Result<PrayerData> {
        return try {
            val today = miqatRepository.getMiqatData(timestampSecs = Date().middayEpochSecs())

            val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
            val prayerTimesMap = mapOf(
                Prayer.FAJR to timeFormat.format(today.fajr),
                Prayer.SUNRISE to timeFormat.format(today.sunrise),
                Prayer.DHUHR to timeFormat.format(today.dhuhr),
                Prayer.ASR to timeFormat.format(today.asr),
                Prayer.MAGHRIB to timeFormat.format(today.maghrib),
                Prayer.ISHAA to timeFormat.format(today.ishaa)
            )

            val gregorianDateInfo = formatGregorianDate(today.gregorian)

            val hijriDateInfo = formatHijriDate(today)

            val nextPrayerInfo = findNextPrayer(today)

            val currentPrayer = findCurrentPrayer(today)

            Result.success(PrayerData(
                prayerTimesMap,
                gregorianDateInfo,
                hijriDateInfo,
                nextPrayerInfo,
                currentPrayer
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun formatGregorianDate(date: Date): DateInfo {
        val dayFormat = SimpleDateFormat("d", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        return DateInfo(
            day = dayFormat.format(date),
            month = monthFormat.format(date),
            year = yearFormat.format(date)
        )
    }

    private fun formatHijriDate(data: MiqatData): DateInfo {
        return try {
            val hijriDate = HijrahChronology.INSTANCE.date(
                data.hijriDate.year,
                data.hijriDate.month,
                data.hijriDate.day,
            )

            val dayFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())
            val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
            val yearFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())

            DateInfo(
                day = localizeDigitsInText(hijriDate.format(dayFormatter)),
                month = hijriDate.format(monthFormatter),
                year = localizeDigitsInText(hijriDate.format(yearFormatter))
            )
        } catch (e: Exception) {
            DateInfo(day = "", month = "", year = "")
        }
    }

    private fun localizeDigitsInText(text: String): String {
        val locale = Locale.getDefault()
        val symbols = DecimalFormatSymbols(locale)
        val zeroDigit = symbols.zeroDigit.code
        val latinZero = '0'.code

        return text.map { char ->
            if (char.isDigit()) {
                (zeroDigit + (char.code - latinZero)).toChar()
            } else {
                char
            }
        }.joinToString("")
    }

    data class DateInfo(
        val day: String,
        val month: String,
        val year: String
    )

    data class PrayerData(
        val prayerTimes: Map<Prayer, String>,
        val gregorianDate: DateInfo,
        val hijriDate: DateInfo,
        val nextPrayer: NextPrayerInfo?,
        val currentPrayer: Prayer?
    )

    data class NextPrayerInfo(
        val prayerName: String,
        val chronometerBaseTime: Long
    )

    enum class Prayer(val stringResId: Int) {
        FAJR(R.string.fajr),
        SUNRISE(R.string.sunrise),
        DHUHR(R.string.dhuhr),
        ASR(R.string.asr),
        MAGHRIB(R.string.maghrib),
        ISHAA(R.string.ishaa)
    }

    private fun findCurrentPrayer(data: MiqatData): Prayer {
        val now = Date()

        return when {
            now >= data.ishaa -> Prayer.ISHAA
            now >= data.maghrib -> Prayer.MAGHRIB
            now >= data.asr -> Prayer.ASR
            now >= data.dhuhr -> Prayer.DHUHR
            now >= data.sunrise -> Prayer.SUNRISE
            now >= data.fajr -> Prayer.FAJR
            else -> {
                Prayer.ISHAA
            }
        }
    }

    private fun findNextPrayer(data: MiqatData): NextPrayerInfo? {
        val now = Date()
        val prayerList = listOf(
            Prayer.FAJR to data.fajr,
            Prayer.SUNRISE to data.sunrise,
            Prayer.DHUHR to data.dhuhr,
            Prayer.ASR to data.asr,
            Prayer.MAGHRIB to data.maghrib,
            Prayer.ISHAA to data.ishaa
        )

        for ((prayer, time) in prayerList) {
            if (time.after(now)) {
                val baseTime = android.os.SystemClock.elapsedRealtime() + (time.time - now.time)

                return NextPrayerInfo(
                    prayerName = prayer.name,
                    chronometerBaseTime = baseTime
                )
            }
        }

        return try {
            val tomorrow = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, 1)
            }.time
            val tomorrowPrayerTimes = miqatRepository.getMiqatData(
                timestampSecs = tomorrow.middayEpochSecs()
            )

            val baseTime = android.os.SystemClock.elapsedRealtime() +
                (tomorrowPrayerTimes.fajr.time - now.time)

            NextPrayerInfo(
                prayerName = Prayer.FAJR.name,
                chronometerBaseTime = baseTime
            )
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Epoch seconds at 12:00 local time on this date's day. miqat keys by the day of the UTC timestamp;
 * anchoring at midday keeps a day near a UTC boundary resolving to the intended date.
 */
private fun Date.middayEpochSecs(): Long {
    val calendar = Calendar.getInstance().apply {
        time = this@middayEpochSecs
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis / 1000
}
