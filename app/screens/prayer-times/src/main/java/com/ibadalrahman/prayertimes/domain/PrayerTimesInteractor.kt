package com.ibadalrahman.prayertimes.domain

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

                val remoteDigest = prayerTimesRepository
                    .fetchDigest(year = action.year)
                    .getOrElse { return@flow }

                val localDigest = prayerTimesRepository.getDigest(year = action.year)

                if (remoteDigest != localDigest) {
                    prayerTimesRepository
                        .fetchPrayerTimes(year = action.year)
                        .getOrElse { return@flow }
                }

                prayerTimesRepository.getDayPrayerTimes(year = 2025, month = 1, day = 1)
                    .fold(
                        onSuccess = { prayerTimes ->
                            emit(PrayerTimesResult.PrayerTimesLoaded(prayerTimes = prayerTimes))
                        },
                        onFailure = {}
                    )
            }
        }
    }
}
