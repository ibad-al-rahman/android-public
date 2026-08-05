package org.ibadalrahman.settings.calculationmethod.domain.entity

import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatData

/**
 * Carries the freshly-persisted method so each screen's reducer can derive its own view state.
 * [preview] holds today's prayer times for the current astronomical config (or `null` when there is
 * no location) so the selection screen can show a live preview.
 */
sealed interface CalculationMethodResult {
    data class Loaded(
        val method: MiqatCalculationMethod,
        val preview: MiqatData?,
    ) : CalculationMethodResult

    /**
     * The user switched to astronomical mode but has never configured one, so there is nothing to
     * restore. The flow needs a location before it can compute anything — the screen navigates to
     * the astronomical method selection so the user can pick one. Mirrors iOS `persistMethodKind`.
     */
    data object RequiresAstronomicalSetup : CalculationMethodResult
}
