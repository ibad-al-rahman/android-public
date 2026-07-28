package org.ibadalrahman.settings.calculationmethod.presenter

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import org.ibadalrahman.settings.calculationmethod.domain.CalculationMethodInteractor
import org.ibadalrahman.settings.calculationmethod.domain.entity.CalculationMethodAction
import org.ibadalrahman.settings.calculationmethod.domain.entity.CalculationMethodResult
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodIntention
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodScreenState
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodViewAction
import javax.inject.Inject

/**
 * Shared by every screen in the calculation-method flow. Because the [MiqatRepository] singleton
 * is the source of truth, separate `hiltViewModel()` instances across nav destinations stay
 * consistent: each screen dispatches [CalculationMethodIntention.Load] on start.
 */
@HiltViewModel
class CalculationMethodViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: CalculationMethodInteractor,
) : BaseViewModel<
        CalculationMethodScreenState,
        CalculationMethodIntention,
        CalculationMethodViewAction,
        CalculationMethodAction,
        CalculationMethodResult
        >(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = CalculationMethodScreenState.Empty,
    interactor = interactor,
) {
    override fun router(
        intention: CalculationMethodIntention
    ): MviBoundary<CalculationMethodViewAction, CalculationMethodAction, CalculationMethodResult> =
        when (intention) {
            CalculationMethodIntention.Load -> action(CalculationMethodAction.Load)
            CalculationMethodIntention.SelectPrecomputed ->
                action(CalculationMethodAction.SelectPrecomputed)
            CalculationMethodIntention.SelectAstronomical ->
                action(CalculationMethodAction.SelectAstronomical)
            is CalculationMethodIntention.SetMethod ->
                action(CalculationMethodAction.SetMethod(intention.method))
            is CalculationMethodIntention.SetMazhab ->
                action(CalculationMethodAction.SetMazhab(intention.mazhab))
            is CalculationMethodIntention.SetAdjustments ->
                action(CalculationMethodAction.SetAdjustments(intention.adjustments))
            is CalculationMethodIntention.SetCoordinates ->
                action(CalculationMethodAction.SetCoordinates(intention.coordinates))
        }

    override fun reduce(result: CalculationMethodResult) {
        updateState { CalculationMethodReducer.reduce(prevState = this, result = result) }
    }
}
