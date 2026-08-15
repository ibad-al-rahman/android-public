package org.ibadalrahman.adhkar.collection.presenter

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.adhkar.collection.domain.AdhkarCollectionInteractor
import org.ibadalrahman.adhkar.collection.domain.entity.AdhkarCollectionAction
import org.ibadalrahman.adhkar.collection.domain.entity.AdhkarCollectionResult
import org.ibadalrahman.adhkar.collection.presenter.entity.AdhkarCollectionIntention
import org.ibadalrahman.adhkar.collection.presenter.entity.AdhkarCollectionScreenState
import org.ibadalrahman.adhkar.collection.presenter.entity.AdhkarCollectionViewAction
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import javax.inject.Inject

@HiltViewModel
class AdhkarCollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: AdhkarCollectionInteractor,
) : BaseViewModel<
        AdhkarCollectionScreenState,
        AdhkarCollectionIntention,
        AdhkarCollectionViewAction,
        AdhkarCollectionAction,
        AdhkarCollectionResult
        >(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = AdhkarCollectionScreenState.Empty,
    interactor = interactor,
) {
    override fun router(
        intention: AdhkarCollectionIntention
    ): MviBoundary<AdhkarCollectionViewAction, AdhkarCollectionAction, AdhkarCollectionResult> =
        when (intention) {
            is AdhkarCollectionIntention.CollectionTapped ->
                viewAction(AdhkarCollectionViewAction.OpenTour(intention.collection))
        }

    override fun reduce(result: AdhkarCollectionResult) {
        updateState { AdhkarCollectionReducer.reduce(prevState = this, result = result) }
    }
}
