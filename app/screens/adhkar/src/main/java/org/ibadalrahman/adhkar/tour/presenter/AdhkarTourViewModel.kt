package org.ibadalrahman.adhkar.tour.presenter

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.adhkar.tour.domain.AdhkarTourInteractor
import org.ibadalrahman.adhkar.tour.domain.entity.AdhkarTourAction
import org.ibadalrahman.adhkar.tour.domain.entity.AdhkarTourResult
import org.ibadalrahman.adhkar.tour.presenter.entity.AdhkarTourIntention
import org.ibadalrahman.adhkar.tour.presenter.entity.AdhkarTourScreenState
import org.ibadalrahman.adhkar.tour.presenter.entity.AdhkarTourViewAction
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import javax.inject.Inject

/**
 * Drives a single tour. The toured collection's slug arrives as a nav argument via
 * [SavedStateHandle] under [COLLECTION_ARG]. Counting and navigation are pure reducer work routed
 * inline; only [AdhkarTourIntention.Load] hits the interactor to resolve the collection.
 */
@HiltViewModel
class AdhkarTourViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: AdhkarTourInteractor,
) : BaseViewModel<
        AdhkarTourScreenState,
        AdhkarTourIntention,
        AdhkarTourViewAction,
        AdhkarTourAction,
        AdhkarTourResult
        >(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = AdhkarTourScreenState.Empty,
    interactor = interactor,
) {
    override fun router(
        intention: AdhkarTourIntention
    ): MviBoundary<AdhkarTourViewAction, AdhkarTourAction, AdhkarTourResult> =
        when (intention) {
            AdhkarTourIntention.Load ->
                action(AdhkarTourAction.Load(savedStateHandle.get<String>(COLLECTION_ARG)))
            AdhkarTourIntention.Tapped -> result(AdhkarTourResult.Tapped)
            AdhkarTourIntention.Next -> result(AdhkarTourResult.Next)
            AdhkarTourIntention.Previous -> result(AdhkarTourResult.Previous)
            AdhkarTourIntention.Finish -> viewAction(AdhkarTourViewAction.Close)
        }

    override fun reduce(result: AdhkarTourResult) {
        updateState { AdhkarTourReducer.reduce(prevState = this, result = result) }
    }

    companion object {
        const val COLLECTION_ARG = "collection"
    }
}
