package com.ibadalrahman.prayertimes.domain

import android.icu.util.Calendar
import com.ibadalrahman.mvi.BaseInteractor
import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesAction
import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date
import javax.inject.Inject

class PrayerTimesInteractor @Inject constructor(
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
            is PrayerTimesAction.OnDateSelected -> getPrayerTimes(date = action.date)
            is PrayerTimesAction.LoadPrayerTimes -> getPrayerTimes(date = action.date)
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

        emit(PrayerTimesResult.PrayerTimesLoaded(prayerTimes = prayerTimes))
    }
}
