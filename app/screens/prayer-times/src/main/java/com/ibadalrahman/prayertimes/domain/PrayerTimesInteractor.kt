package com.ibadalrahman.prayertimes.domain

import android.content.Context
import android.icu.util.Calendar
import com.ibadalrahman.mvi.BaseInteractor
import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesAction
import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.text.DecimalFormatSymbols
import java.time.chrono.HijrahChronology
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import com.ibadalrahman.resources.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZoneId

class PrayerTimesInteractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prayerTimesRepository: PrayerTimesRepository
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
        emit(PrayerTimesResult.Loading)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val remoteDigest = prayerTimesRepository
            .fetchDigest(year = year)
            .getOrElse { return@flow }

        val localDigest = prayerTimesRepository.getDigest(year = year)

        if (remoteDigest != localDigest) {
            prayerTimesRepository
                .fetchPrayerTimes(year = year)
                .getOrElse { return@flow }
        }

        val prayerTimes = prayerTimesRepository
            .getDayPrayerTimes(year = year, month = month, day = day)
            .getOrElse { return@flow }

        val weekPrayerTimes = prayerTimesRepository
            .getWeekPrayerTimes(weekId = prayerTimes.weekId)
            .getOrElse { return@flow }

        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val hijriDate = HijrahChronology.INSTANCE.date(
            inputFormatter.withChronology(HijrahChronology.INSTANCE).
            parse(prayerTimes.hijri)
        )

        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

        val hijriDateFormatted = localizeDigitsInText(
            formatter.format(hijriDate), Locale.getDefault()
        )
        val prayerTimesWithHijri = prayerTimes.copy(hijri = hijriDateFormatted)

        emit(PrayerTimesResult.PrayerTimesLoaded(
            prayerTimes = prayerTimesWithHijri,
            weekPrayerTimes = weekPrayerTimes
        ))
    }

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
