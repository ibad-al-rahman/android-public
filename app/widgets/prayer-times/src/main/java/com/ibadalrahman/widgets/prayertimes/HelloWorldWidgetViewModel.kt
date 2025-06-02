package com.ibadalrahman.widgets.prayertimes

import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import com.ibadalrahman.resources.R
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
            Result.success(PrayerData(prayerTimesMap, dateFormat.format(dailyPrayerTimes.gregorian)))
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
        val date: String
    )

    enum class Prayer(val stringResId: Int) {
        FAJR(R.string.fajr),
        SUNRISE(R.string.sunrise),
        DHUHR(R.string.dhuhr),
        ASR(R.string.asr),
        MAGHRIB(R.string.maghrib),
        ISHAA(R.string.ishaa)
    }
}
