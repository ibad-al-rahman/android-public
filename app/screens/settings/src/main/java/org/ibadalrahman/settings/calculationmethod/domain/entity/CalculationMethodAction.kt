package org.ibadalrahman.settings.calculationmethod.domain.entity

import org.ibadalrahman.miqat.Coordinates
import org.ibadalrahman.miqat.Mazhab
import org.ibadalrahman.miqat.TimeAdjustment
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalMethod

/**
 * Actions shared by every screen in the calculation-method flow. Each performs a synchronous
 * read/mutate/write against the miqat repository and reloads the resulting method.
 */
sealed interface CalculationMethodAction {
    /** Reload the persisted method into state (on screen start). */
    data object Load : CalculationMethodAction
    data object SelectPrecomputed : CalculationMethodAction
    data object SelectAstronomical : CalculationMethodAction
    data class SetMethod(val method: AstronomicalMethod) : CalculationMethodAction
    data class SetMazhab(val mazhab: Mazhab) : CalculationMethodAction
    data class SetAdjustments(val adjustments: TimeAdjustment) : CalculationMethodAction
    data class SetCoordinates(val coordinates: Coordinates) : CalculationMethodAction
}
