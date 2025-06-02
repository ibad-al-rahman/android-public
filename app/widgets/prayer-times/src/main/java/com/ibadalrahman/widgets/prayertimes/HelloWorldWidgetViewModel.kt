package com.ibadalrahman.widgets.prayertimes

import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import com.ibadalrahman.prayertimes.repository.data.domain.PrayerTimes
import com.ibadalrahman.resources.R
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HelloWorldWidgetViewModel @Inject constructor(
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

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val prayerTimesMap = mapOf(
                Prayer.FAJR to timeFormat.format(dailyPrayerTimes.prayerTimes.fajr),
                Prayer.SUNRISE to timeFormat.format(dailyPrayerTimes.prayerTimes.sunrise),
                Prayer.DHUHR to timeFormat.format(dailyPrayerTimes.prayerTimes.dhuhr),
                Prayer.ASR to timeFormat.format(dailyPrayerTimes.prayerTimes.asr),
                Prayer.MAGHRIB to timeFormat.format(dailyPrayerTimes.prayerTimes.maghrib),
                Prayer.ISHAA to timeFormat.format(dailyPrayerTimes.prayerTimes.ishaa)
            )

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Calculate next prayer
            val nextPrayerInfo = findNextPrayer(
                dailyPrayerTimes.prayerTimes,
                prayerTimesRepository,
                year, month, day
            )

            val currentPrayer = findCurrentPrayer(dailyPrayerTimes.prayerTimes)

            Result.success(PrayerData(
                prayerTimesMap,
                dateFormat.format(dailyPrayerTimes.gregorian),
                dailyPrayerTimes.hijri,
                nextPrayerInfo,
                currentPrayer
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getLocalizedTime(time: String, locale: Locale = Locale.getDefault()): String {
        val symbols = DecimalFormatSymbols(locale)
        val zeroDigit = symbols.zeroDigit
        return time.map { c ->
            if (c in '0'..'9') {
                (zeroDigit + (c - '0'))
            } else {
                c
            }
        }.joinToString("")
    }

    data class PrayerData(
        val prayerTimes: Map<Prayer, String>,
        val date: String,
        val hijriDate: String,
        val nextPrayer: NextPrayerInfo?,
        val currentPrayer: Prayer?
    )

    data class NextPrayerInfo(
        val prayerName: String,
        val chroneterBaseTime: Long
    )

    enum class Prayer(val stringResId: Int) {
        FAJR(R.string.fajr),
        SUNRISE(R.string.sunrise),
        DHUHR(R.string.dhuhr),
        ASR(R.string.asr),
        MAGHRIB(R.string.maghrib),
        ISHAA(R.string.ishaa)
    }

    private fun findCurrentPrayer(prayerTimes: PrayerTimes): Prayer? {
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
        repository: PrayerTimesRepository,
        year: Int,
        month: Int,
        day: Int
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
                    chroneterBaseTime = baseTime
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
                    chroneterBaseTime = baseTime
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}
