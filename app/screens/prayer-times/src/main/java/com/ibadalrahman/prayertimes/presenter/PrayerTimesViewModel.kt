package com.ibadalrahman.prayertimes.presenter

import androidx.lifecycle.SavedStateHandle
import com.ibadalrahman.mvi.BaseViewModel
import com.ibadalrahman.mvi.MviBoundary
import com.ibadalrahman.prayertimes.domain.PrayerTimesInteractor
import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesAction
import com.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import com.ibadalrahman.prayertimes.presenter.entity.PrayerTimesViewAction
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.base.CoroutineDispatchers
import javax.inject.Inject

@HiltViewModel
class PrayerTimesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: PrayerTimesInteractor
): BaseViewModel<
    PrayerTimesScreenState,
    PrayerTimesIntention,
    PrayerTimesViewAction,
    PrayerTimesAction,
    PrayerTimesResult
>(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = PrayerTimesScreenState.initialState,
    interactor = interactor
) {
    override fun router(
        intention: PrayerTimesIntention
    ): MviBoundary<PrayerTimesViewAction, PrayerTimesAction, PrayerTimesResult> =
        MviBoundary.Result(PrayerTimesResult.Noop)

    override fun reduce(result: PrayerTimesResult) {}

    override fun viewActionFrom(result: PrayerTimesResult): PrayerTimesViewAction? = null
}
