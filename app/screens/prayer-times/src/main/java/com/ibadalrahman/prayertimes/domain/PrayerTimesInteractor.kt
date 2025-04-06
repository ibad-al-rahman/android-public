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
            is PrayerTimesAction.LoadPrayerTimes -> {
                emit(PrayerTimesResult.Loading)
                prayerTimesRepository
                    .fetchDigest(year = action.year)
                    .fold(
                        onSuccess = { digest ->
                            val localDigest = prayerTimesRepository.getDigest(year = action.year)
                            if(digest != localDigest) {
                                prayerTimesRepository
                                    .fetchPrayerTimes(year = action.year)
                                    .fold(
                                        onSuccess = {
                                            prayerTimesRepository
                                                .getDayPrayerTimes(year = 2025, month = 1, day = 1)
                                                .fold(
                                                    onSuccess = {
                                                        emit(PrayerTimesResult.PrayerTimesLoaded(prayerTimes = it))
                                                    },
                                                    onFailure = {}
                                                )
                                        },
                                        onFailure = {}
                                    )
                            }
                        },
                        onFailure = {
                            println(it)
                        }
                    )
            }
        }
    }
}
