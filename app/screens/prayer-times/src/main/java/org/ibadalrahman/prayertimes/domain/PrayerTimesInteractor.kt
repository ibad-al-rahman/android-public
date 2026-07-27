package org.ibadalrahman.prayertimes.domain

import android.content.Context
import android.icu.util.Calendar
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.ibadalrahman.miqat.IslamicEvent
import org.ibadalrahman.miqat.repository.MiqatRepository
import org.ibadalrahman.miqat.repository.data.domain.MiqatData
import org.ibadalrahman.mvi.BaseInteractor
import org.ibadalrahman.prayertimes.domain.entity.PrayerTimesAction
import org.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import org.ibadalrahman.resources.R
import java.text.DateFormat
import java.text.DecimalFormatSymbols
import java.time.ZoneId
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PrayerTimesInteractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val miqatRepository: MiqatRepository
): BaseInteractor<PrayerTimesAction, PrayerTimesResult> {
    override suspend fun resultFrom(action: PrayerTimesAction): Flow<PrayerTimesResult> =
        when(action) {
            PrayerTimesAction.ShowDatePicker -> {
                flowOf(PrayerTimesResult.ShowDatePicker)
            }
            PrayerTimesAction.HideDatePicker -> {
                flowOf(PrayerTimesResult.HideDatePicker)
            }
            PrayerTimesAction.ShowDailyView -> {
                flowOf(PrayerTimesResult.ShowDailyView)
            }
            PrayerTimesAction.ShowWeeklyView -> {
                flowOf(PrayerTimesResult.ShowWeeklyView)
            }
            is PrayerTimesAction.OnDateSelected -> getPrayerTimes(date = action.date)
            is PrayerTimesAction.LoadPrayerTimes -> getPrayerTimes(date = action.date)
            is PrayerTimesAction.Share -> prepareShareText(state = action.state)
        }

    private fun getPrayerTimes(date: Date): Flow<PrayerTimesResult> = flow {
        val day = miqatRepository.getMiqatData(timestampSecs = date.middayEpochSecs())

        val week = weekSaturdays(date).map { saturdayOffsetDate ->
            miqatRepository.getMiqatData(timestampSecs = saturdayOffsetDate.middayEpochSecs())
        }

        emit(
            PrayerTimesResult.PrayerTimesLoaded(
                day = day,
                dayHijri = formatHijriDate(day),
                event = localizedEvent(day),
                week = week,
            )
        )
    }

    /** The seven days of the Sat→Fri week containing [date], in order. */
    private fun weekSaturdays(date: Date): List<Date> {
        val saturday = Calendar.getInstance().apply {
            time = date
            // Calendar.SATURDAY == 7; walk back to the most recent Saturday.
            val diff = (get(Calendar.DAY_OF_WEEK) - Calendar.SATURDAY + 7) % 7
            add(Calendar.DAY_OF_MONTH, -diff)
        }
        return (0..6).map { offset ->
            (saturday.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, offset)
            }.time
        }
    }

    private fun formatHijriDate(day: MiqatData): String {
        val hijriDate = HijrahChronology.INSTANCE.date(
            day.hijriDate.year,
            day.hijriDate.month,
            day.hijriDate.day,
        )
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return localizeDigitsInText(formatter.format(hijriDate), Locale.getDefault())
    }

    private fun localizedEvent(day: MiqatData): String? =
        day.islamicEvents.firstOrNull()?.let { context.getString(it.labelRes) }

    private fun prepareShareText(state: PrayerTimesScreenState) = flow {
        if (state.prayerTimes == null) {
            return@flow
        }

        val localDate = state.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
        val gregorianDateFormatted = localizeDigitsInText(
            localDate.format(dateFormatter), Locale.getDefault()
        )

        val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT)

        val text = listOf(
            context.getString(R.string.share_msg_header),
            context.getString(R.string.share_msg_date, gregorianDateFormatted, state.prayerTimes.hijriDate),
            context.getString(R.string.share_msg_fajr, timeFormatter.format(state.prayerTimes.fajr)),
            context.getString(R.string.share_msg_sunrise, timeFormatter.format(state.prayerTimes.sunrise)),
            context.getString(R.string.share_msg_dhuhr, timeFormatter.format(state.prayerTimes.dhuhr)),
            context.getString(R.string.share_msg_asr, timeFormatter.format(state.prayerTimes.asr)),
            context.getString(R.string.share_msg_maghrib, timeFormatter.format(state.prayerTimes.maghrib)),
            context.getString(R.string.share_msg_ishaa, timeFormatter.format(state.prayerTimes.ishaa)),
        ).joinToString("\n\n")

        emit(PrayerTimesResult.ShareTextProcessed(text = text))
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

/** Localized display name for an [IslamicEvent]. */
private val IslamicEvent.labelRes: Int
    get() = when (this) {
        IslamicEvent.ISLAMIC_NEW_YEAR -> R.string.event_islamic_new_year
        IslamicEvent.ASHURA -> R.string.event_ashura
        IslamicEvent.MAWLID_AL_NABI -> R.string.event_mawlid_al_nabi
        IslamicEvent.BATTLE_OF_HATTIN -> R.string.event_battle_of_hattin
        IslamicEvent.BATTLE_OF_MUTAH -> R.string.event_battle_of_mutah
        IslamicEvent.BATTLE_OF_TABUK -> R.string.event_battle_of_tabuk
        IslamicEvent.ISRA_AND_MIRAJ -> R.string.event_isra_and_miraj
        IslamicEvent.NISF_SHABAN -> R.string.event_nisf_shaban
        IslamicEvent.FIRST_OF_RAMADAN -> R.string.event_first_of_ramadan
        IslamicEvent.BATTLE_OF_BADR -> R.string.event_battle_of_badr
        IslamicEvent.CONQUEST_OF_MECCA -> R.string.event_conquest_of_mecca
        IslamicEvent.LAYLAT_AL_QADR -> R.string.event_laylat_al_qadr
        IslamicEvent.EID_AL_FITR -> R.string.event_eid_al_fitr
        IslamicEvent.BATTLE_OF_UHUD -> R.string.event_battle_of_uhud
        IslamicEvent.DAY_OF_ARAFAH -> R.string.event_day_of_arafah
        IslamicEvent.EID_AL_ADHA -> R.string.event_eid_al_adha
    }
