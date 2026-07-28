package org.ibadalrahman.settings.calculationmethod.view

import androidx.compose.runtime.Composable
import org.ibadalrahman.mvi.ObserveLifecycleEvents
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodIntention

/**
 * Reloads the persisted method into state on every ON_RESUME. Because the calculation-method
 * screens share the [MiqatRepository] singleton but each holds its own ViewModel instance,
 * reloading on resume keeps a screen fresh after a child screen (e.g. location search) changed
 * the config and popped back.
 */
@Composable
internal fun LoadOnce(intentionProcessor: (CalculationMethodIntention) -> Unit) {
    ObserveLifecycleEvents(
        onResume = { intentionProcessor(CalculationMethodIntention.Load) }
    )
}
