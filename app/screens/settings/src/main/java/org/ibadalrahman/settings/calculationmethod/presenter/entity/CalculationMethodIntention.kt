package org.ibadalrahman.settings.calculationmethod.presenter.entity

import org.ibadalrahman.miqat.Coordinates
import org.ibadalrahman.miqat.Mazhab
import org.ibadalrahman.miqat.TimeAdjustment
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalMethod

sealed interface CalculationMethodIntention {
    data object Load : CalculationMethodIntention
    data object SelectPrecomputed : CalculationMethodIntention
    data object SelectAstronomical : CalculationMethodIntention
    data class SetMethod(val method: AstronomicalMethod) : CalculationMethodIntention
    data class SetMazhab(val mazhab: Mazhab) : CalculationMethodIntention
    data class SetAdjustments(val adjustments: TimeAdjustment) : CalculationMethodIntention
    data class SetCoordinates(val coordinates: Coordinates) : CalculationMethodIntention
}
