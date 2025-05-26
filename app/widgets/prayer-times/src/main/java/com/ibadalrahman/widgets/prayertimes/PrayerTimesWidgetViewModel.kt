package com.ibadalrahman.widgets.prayertimes

import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import com.ibadalrahman.prayertimes.repository.data.domain.DayPrayerTimes
import java.text.DecimalFormatSymbols
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PrayerTimesWidgetViewModel @Inject constructor(
    private val prayerTimesRepository: PrayerTimesRepository
) {
    suspend fun getPrayerTimes(): Result<DayPrayerTimes> {
        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dailyPrayerTimes = prayerTimesRepository
            .getDayPrayerTimes(year, month, day)
            .getOrElse { return Result.failure(it) }
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val hijriDate = HijrahChronology.INSTANCE.date(
            inputFormatter.withChronology(HijrahChronology.INSTANCE).
            parse(dailyPrayerTimes.hijri)
        )
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val hijriDateFormatted = localizeDigitsInText(
            formatter.format(hijriDate), Locale.getDefault()
        )

        return Result.success(dailyPrayerTimes.copy(hijri = hijriDateFormatted))
    }

    private fun localizeDigitsInText(text: String, locale: Locale): String {
        val symbols = DecimalFormatSymbols(locale)
        val zeroDigit = symbols.zeroDigit
        return text.map { c ->
            if (c in '0'..'9') {
                (zeroDigit + (c - '0'))
            } else {
                c
            }
        }.joinToString("")
    }
}
