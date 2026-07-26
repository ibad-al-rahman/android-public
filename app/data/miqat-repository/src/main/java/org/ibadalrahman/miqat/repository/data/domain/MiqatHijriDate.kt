package org.ibadalrahman.miqat.repository.data.domain

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.IslamicCalendar
import org.ibadalrahman.miqat.HijriDate
import java.util.Locale

/**
 * Islamic (Hijri) calendar date. Port of iOS `MiqatHijriDate`.
 *
 * The library exposes `month`/`day` as unsigned bytes; here they are widened to [Int].
 */
data class MiqatHijriDate(
    val day: Int,
    val month: Int,
    val year: Int,
) {
    /**
     * The localized full month name (e.g. "Ramadan"), or `null` if it cannot be resolved.
     * Uses the Umm al-Qura Islamic calendar, mirroring iOS.
     */
    val localeMonth: String?
        get() = runCatching {
            val calendar = IslamicCalendar().apply {
                set(Calendar.MONTH, month - 1)
            }
            SimpleDateFormat("MMMM", Locale.getDefault()).apply {
                this.calendar = calendar
            }.format(calendar.time)
        }.getOrNull()

    companion object {
        fun from(hijriDate: HijriDate): MiqatHijriDate = MiqatHijriDate(
            day = hijriDate.day.toInt(),
            month = hijriDate.month.toInt(),
            year = hijriDate.year,
        )
    }
}
