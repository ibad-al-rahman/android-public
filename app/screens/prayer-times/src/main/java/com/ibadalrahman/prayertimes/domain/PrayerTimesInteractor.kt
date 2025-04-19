package com.ibadalrahman.prayertimes.domain

import android.icu.util.Calendar
import com.ibadalrahman.mvi.BaseInteractor
import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesAction
import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PrayerTimesInteractor @Inject constructor(
    private val prayerTimesRepository: PrayerTimesRepository
): BaseInteractor<PrayerTimesAction, PrayerTimesResult> {
    override suspend fun resultFrom(action: PrayerTimesAction): Flow<PrayerTimesResult> = flow {
        when(action) {
            PrayerTimesAction.ShowDatePicker -> {
                emit(PrayerTimesResult.ShowDatePicker)
            }
            PrayerTimesAction.HideDatePicker -> {
                emit(PrayerTimesResult.HideDatePicker)
            }
            is PrayerTimesAction.LoadPrayerTimes -> {
                emit(PrayerTimesResult.Loading)
                val calendar = Calendar.getInstance()
                calendar.time = action.date

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
    }
}
