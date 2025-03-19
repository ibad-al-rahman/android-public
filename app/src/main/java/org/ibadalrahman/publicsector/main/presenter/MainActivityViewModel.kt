package org.ibadalrahman.publicsector.main.presenter

import android.R
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ibadalrahman.mvi.BaseViewModel
import com.ibadalrahman.mvi.MviBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.publicsector.main.domain.MainAction
import org.ibadalrahman.publicsector.main.domain.MainActivityInteractor
import org.ibadalrahman.publicsector.main.domain.MainResult
import org.ibadalrahman.publicsector.main.model.PrayerPage
import org.ibadalrahman.publicsector.main.model.PrayerRepository
import org.ibadalrahman.publicsector.main.model.PrayerRepositoryImpl
import org.ibadalrahman.publicsector.main.model.ViewState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: MainActivityInteractor,
): BaseViewModel<MainState, MainIntention, MainViewAction, MainAction, MainResult>(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = MainState(),
    interactor = interactor
) {

    override fun router(intention: MainIntention): MviBoundary<MainViewAction, MainAction, MainResult> = action(MainAction.Noop)

    override fun reduce(result: MainResult) { }

//    custom
    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    private val repository: PrayerRepository = PrayerRepositoryImpl()


    init {
        handleIntent(MainIntention.LoadDayView)
    }

    fun handleIntent(intent: MainIntention) {
        when (intent) {
            is MainIntention.LoadDayView -> loadDayView()
            is MainIntention.LoadWeekView -> loadWeekView()
            is MainIntention.SetDate -> setDate(intent.inputDate)
            is MainIntention.LoadPrayers -> loadDailyPrayers()
        }
    }


    private fun loadDayView() {
        var currentDate = Date()
        var sdf = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ROOT)
        var currentDateFormatted : String = sdf.format(currentDate)

        _viewState.value = _viewState.value.copy(
            currentPrayerPage = PrayerPage.DAILY,
            inputDate = currentDateFormatted
        )
//        reload
        loadDailyPrayers()

    }

    private fun setDate(inputDate: String) {
//        set state to "loading"
        _viewState.value = _viewState.value.copy(inputDate = inputDate)

        loadDailyPrayers()
    }

    private fun loadWeekView() {
        _viewState.value = _viewState.value.copy(
            isLoading = true,
            currentPrayerPage = PrayerPage.WEEKLY
        )

        val week_no = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)

        viewModelScope.launch {
            val prayersWeek = repository.getPrayersForWeek(week_no)
            _viewState.value = _viewState.value.copy(
                isLoading = false,
                prayersWeek = prayersWeek
            )
        }

    }


    private fun loadDailyPrayers() {
        _viewState.value = _viewState.value.copy(
            isLoading = true,
            currentPrayerPage = PrayerPage.DAILY
        )

        viewModelScope.launch {
            val prayersDay = repository.getPrayersForDay(_viewState.value.inputDate)
            _viewState.value = _viewState.value.copy(
                isLoading = false,
                prayersDay = prayersDay
            )
        }
    }
}
