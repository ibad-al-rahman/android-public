package org.ibadalrahman.settings.calculationmethod.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatData

/**
 * Shared state for the calculation-method flow. Holds the persisted [method]; each screen reads
 * the slice it cares about. `null` while the first [Load] is in flight.
 *
 * [preview] carries today's prayer times for the current astronomical config so the selection
 * screen can show a live Fajr/Ishaa preview; it is `null` when no location is set (precomputed or
 * before the first location is chosen).
 */
@Stable
@Immutable
data class CalculationMethodScreenState(
    val method: MiqatCalculationMethod?,
    val preview: MiqatData? = null,
) {
    val isAstronomical: Boolean get() = method is MiqatCalculationMethod.Astronomical

    val hasLocation: Boolean get() = method?.asAstronomical != null

    companion object {
        val Empty = CalculationMethodScreenState(method = null)
    }
}
