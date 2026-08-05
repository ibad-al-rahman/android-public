package org.ibadalrahman.settings.calculationmethod.presenter

import org.ibadalrahman.settings.calculationmethod.domain.entity.CalculationMethodResult
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodScreenState

object CalculationMethodReducer {
    fun reduce(
        prevState: CalculationMethodScreenState,
        result: CalculationMethodResult
    ): CalculationMethodScreenState = when (result) {
        is CalculationMethodResult.Loaded ->
            prevState.copy(method = result.method, preview = result.preview)
        // Navigation-only; the view action drives it, state is untouched.
        CalculationMethodResult.RequiresAstronomicalSetup -> prevState
    }
}
