package org.ibadalrahman.widgets.prayertimes

import org.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import org.ibadalrahman.prayertimes.repository.data.domain.PrayerTimes
import org.ibadalrahman.resources.R
import kotlinx.coroutines.runBlocking
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
    private val prayerTimesRepository: PrayerTimesRepository
) {
    suspend fun getPrayerTimes(): Result<PrayerData> {
        return try {
            val today = Date()
            val calendar = Calendar.getInstance()
            calendar.time = today
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dailyPrayerTimes = prayerTimesRepository
                .getDayPrayerTimes(year, month, day)
                .getOrElse { return Result.failure(it) }

            val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
            val prayerTimesMap = mapOf(
                Prayer.FAJR to timeFormat.format(dailyPrayerTimes.prayerTimes.fajr),
                Prayer.SUNRISE to timeFormat.format(dailyPrayerTimes.prayerTimes.sunrise),
                Prayer.DHUHR to timeFormat.format(dailyPrayerTimes.prayerTimes.dhuhr),
                Prayer.ASR to timeFormat.format(dailyPrayerTimes.prayerTimes.asr),
                Prayer.MAGHRIB to timeFormat.format(dailyPrayerTimes.prayerTimes.maghrib),
                Prayer.ISHAA to timeFormat.format(dailyPrayerTimes.prayerTimes.ishaa)
            )

            val gregorianDateInfo = formatGregorianDate(dailyPrayerTimes.gregorian)

            val hijriDateInfo = formatHijriDate(dailyPrayerTimes.hijri)

            val nextPrayerInfo = findNextPrayer(
                dailyPrayerTimes.prayerTimes,
                prayerTimesRepository
            )

            val currentPrayer = findCurrentPrayer(dailyPrayerTimes.prayerTimes)

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

    private fun formatHijriDate(hijriDateString: String): DateInfo {
        return try {
            val dateParts = hijriDateString.split("/")
            if (dateParts.size == 3) {
                val day = dateParts[0].toInt()
                val month = dateParts[1].toInt()
                val year = dateParts[2].toInt()

                val hijriDate = HijrahChronology.INSTANCE.date(year, month, day)

                val dayFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())
                val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
                val yearFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())

                DateInfo(
                    day = localizeDigitsInText(hijriDate.format(dayFormatter)),
                    month = hijriDate.format(monthFormatter),
                    year = localizeDigitsInText(hijriDate.format(yearFormatter))
                )
            } else {
                DateInfo(
                    day = "",
                    month = hijriDateString,
                    year = ""
                )
            }
        } catch (e: Exception) {
            DateInfo(
                day = "",
                month = hijriDateString,
                year = ""
            )
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

    private fun findCurrentPrayer(prayerTimes: PrayerTimes): Prayer {
        val now = Date()

        return when {
            now >= prayerTimes.ishaa -> Prayer.ISHAA
            now >= prayerTimes.maghrib -> Prayer.MAGHRIB
            now >= prayerTimes.asr -> Prayer.ASR
            now >= prayerTimes.dhuhr -> Prayer.DHUHR
            now >= prayerTimes.sunrise -> Prayer.SUNRISE
            now >= prayerTimes.fajr -> Prayer.FAJR
            else -> {
                Prayer.ISHAA
            }
        }
    }

    private fun findNextPrayer(
        prayerTimes: PrayerTimes,
        repository: PrayerTimesRepository
    ): NextPrayerInfo? {
        val now = Date()
        val prayerList = listOf(
            Prayer.FAJR to prayerTimes.fajr,
            Prayer.SUNRISE to prayerTimes.sunrise,
            Prayer.DHUHR to prayerTimes.dhuhr,
            Prayer.ASR to prayerTimes.asr,
            Prayer.MAGHRIB to prayerTimes.maghrib,
            Prayer.ISHAA to prayerTimes.ishaa
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
            val tomorrowCalendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, 1)
            }
            val tomorrowPrayerTimes = runBlocking {
                repository.getDayPrayerTimes(
                    tomorrowCalendar.get(Calendar.YEAR),
                    tomorrowCalendar.get(Calendar.MONTH) + 1,
                    tomorrowCalendar.get(Calendar.DAY_OF_MONTH)
                ).getOrNull()
            }

            tomorrowPrayerTimes?.let {
                val baseTime = android.os.SystemClock.elapsedRealtime() + (it.prayerTimes.fajr.time - now.time)

                NextPrayerInfo(
                    prayerName = Prayer.FAJR.name,
                    chronometerBaseTime = baseTime
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}
