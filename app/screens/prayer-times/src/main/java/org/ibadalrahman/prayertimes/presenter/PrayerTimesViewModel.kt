package org.ibadalrahman.prayertimes.presenter

import androidx.lifecycle.SavedStateHandle
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import org.ibadalrahman.prayertimes.domain.PrayerTimesInteractor
import org.ibadalrahman.prayertimes.domain.entity.PrayerTimesAction
import org.ibadalrahman.prayertimes.domain.entity.PrayerTimesResult
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesIntention
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesScreenState
import org.ibadalrahman.prayertimes.presenter.entity.PrayerTimesViewAction
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
            is PrayerTimesIntention.OnTapShare ->
                action(PrayerTimesAction.Share(state = intention.state))
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

    override fun viewActionFrom(result: PrayerTimesResult): PrayerTimesViewAction? = when(result) {
        is PrayerTimesResult.ShareTextProcessed -> PrayerTimesViewAction.Share(text = result.text)
        else -> null
    }
}
