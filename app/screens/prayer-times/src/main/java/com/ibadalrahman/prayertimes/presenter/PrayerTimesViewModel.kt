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
import java.util.Date
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
        when(intention) {
            PrayerTimesIntention.OnScreenStarted ->
                action(PrayerTimesAction.LoadPrayerTimes(date = Date()))
            PrayerTimesIntention.OnTapShowDatePicker -> action(PrayerTimesAction.ShowDatePicker)
            PrayerTimesIntention.OnDismissDatePicker -> action(PrayerTimesAction.HideDatePicker)
            PrayerTimesIntention.OnTapDailyView -> action(PrayerTimesAction.ShowDailyView)
            PrayerTimesIntention.OnTapWeeklyView -> action(PrayerTimesAction.ShowWeeklyView)
            is PrayerTimesIntention.OnDateSelected ->
                action(PrayerTimesAction.OnDateSelected(date = intention.date))
        }

    override fun reduce(result: PrayerTimesResult) {
        updateState { PrayerTimesReducer.reduce(prevState = this, result = result) }
    }

    override fun viewActionFrom(result: PrayerTimesResult): PrayerTimesViewAction? = null
}
