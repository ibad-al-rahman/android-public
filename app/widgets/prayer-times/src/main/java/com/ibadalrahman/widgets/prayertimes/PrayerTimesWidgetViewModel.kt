package com.ibadalrahman.widgets.prayertimes

import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import com.ibadalrahman.prayertimes.repository.data.domain.PrayerTimes
import com.ibadalrahman.resources.R
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

            // Format Gregorian date
            val gregorianDateInfo = formatGregorianDate(dailyPrayerTimes.gregorian)

            // Format Hijri date
            val hijriDateInfo = formatHijriDate(dailyPrayerTimes.hijri)

            // Calculate next prayer
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
            // Parse the hijri date string (assuming format "dd/MM/yyyy")
            val dateParts = hijriDateString.split("/")
            if (dateParts.size == 3) {
                val day = dateParts[0].toInt()
                val month = dateParts[1].toInt()
                val year = dateParts[2].toInt()

                // Create LocalDate and convert to Hijri
                val hijriDate = HijrahChronology.INSTANCE.date(year, month, day)

                // Format each part separately
                val dayFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())
                val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
                val yearFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())

                DateInfo(
                    day = localizeDigitsInText(hijriDate.format(dayFormatter)),
                    month = hijriDate.format(monthFormatter),
                    year = localizeDigitsInText(hijriDate.format(yearFormatter))
                )
            } else {
                // Fallback if parsing fails
                DateInfo(
                    day = "",
                    month = hijriDateString,
                    year = ""
                )
            }
        } catch (e: Exception) {
            // Fallback if any error occurs
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

        // Check in reverse order to find which prayer period we're in
        return when {
            now >= prayerTimes.ishaa -> Prayer.ISHAA
            now >= prayerTimes.maghrib -> Prayer.MAGHRIB
            now >= prayerTimes.asr -> Prayer.ASR
            now >= prayerTimes.dhuhr -> Prayer.DHUHR
            now >= prayerTimes.sunrise -> Prayer.SUNRISE
            now >= prayerTimes.fajr -> Prayer.FAJR
            else -> {
                // Before Fajr, we're in the previous day's Ishaa period
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

        // Find next prayer today
        for ((prayer, time) in prayerList) {
            if (time.after(now)) {
                // Calculate base time for countdown chronometer
                val baseTime = android.os.SystemClock.elapsedRealtime() + (time.time - now.time)

                return NextPrayerInfo(
                    prayerName = prayer.name,
                    chronometerBaseTime = baseTime
                )
            }
        }

        // If no prayer left today, get tomorrow's Fajr
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
                // Calculate base time for countdown chronometer
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
