package org.ibadalrahman.settings.calculationmethod.presenter

import org.ibadalrahman.settings.calculationmethod.domain.entity.CalculationMethodResult
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodScreenState

object CalculationMethodReducer {
    fun reduce(
        prevState: CalculationMethodScreenState,
        result: CalculationMethodResult
    ): CalculationMethodScreenState = when (result) {
        is CalculationMethodResult.Loaded -> prevState.copy(method = result.method)
    }
}
