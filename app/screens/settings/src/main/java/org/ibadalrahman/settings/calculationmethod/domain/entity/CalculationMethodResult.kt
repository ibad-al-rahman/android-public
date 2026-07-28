package org.ibadalrahman.settings.calculationmethod.domain.entity

import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod

/** Carries the freshly-persisted method so each screen's reducer can derive its own view state. */
sealed interface CalculationMethodResult {
    data class Loaded(val method: MiqatCalculationMethod) : CalculationMethodResult
}
