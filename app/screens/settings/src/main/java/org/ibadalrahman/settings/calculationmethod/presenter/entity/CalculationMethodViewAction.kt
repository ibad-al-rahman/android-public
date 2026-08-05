package org.ibadalrahman.settings.calculationmethod.presenter.entity

sealed interface CalculationMethodViewAction {
    /**
     * The user picked astronomical mode without an existing config. Navigate to the astronomical
     * method selection so a location can be chosen. Mirrors iOS pushing the options screen.
     */
    data object OpenAstronomicalMethod : CalculationMethodViewAction
}
