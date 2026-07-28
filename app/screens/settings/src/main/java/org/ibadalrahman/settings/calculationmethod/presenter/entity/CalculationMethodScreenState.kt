package org.ibadalrahman.settings.calculationmethod.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod

/**
 * Shared state for the calculation-method flow. Holds the persisted [method]; each screen reads
 * the slice it cares about. `null` while the first [Load] is in flight.
 */
@Stable
@Immutable
data class CalculationMethodScreenState(
    val method: MiqatCalculationMethod?,
) {
    val isAstronomical: Boolean get() = method is MiqatCalculationMethod.Astronomical

    val hasLocation: Boolean get() = method?.asAstronomical != null

    companion object {
        val Empty = CalculationMethodScreenState(method = null)
    }
}
