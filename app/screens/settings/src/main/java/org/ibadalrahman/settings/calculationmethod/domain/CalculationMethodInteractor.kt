package org.ibadalrahman.settings.calculationmethod.domain

import android.icu.util.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.ibadalrahman.miqat.Coordinates
import org.ibadalrahman.miqat.Method
import org.ibadalrahman.miqat.repository.MiqatRepository
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalConfig
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.mvi.BaseInteractor
import org.ibadalrahman.settings.calculationmethod.domain.entity.CalculationMethodAction
import org.ibadalrahman.settings.calculationmethod.domain.entity.CalculationMethodResult
import javax.inject.Inject

/**
 * Read/mutate/write against the [MiqatRepository] singleton — the single source of truth for the
 * whole calculation-method flow. Every action persists and then reloads the method, so each
 * screen's reducer always sees a fresh, consistent [MiqatCalculationMethod].
 */
class CalculationMethodInteractor @Inject constructor(
    private val miqatRepository: MiqatRepository,
) : BaseInteractor<CalculationMethodAction, CalculationMethodResult> {

    override suspend fun resultFrom(
        action: CalculationMethodAction
    ): Flow<CalculationMethodResult> {
        when (action) {
            CalculationMethodAction.Load -> Unit
            CalculationMethodAction.SelectPrecomputed ->
                miqatRepository.setCalculationMethod(MiqatCalculationMethod.default)
            CalculationMethodAction.SelectAstronomical ->
                // No config to restore yet: signal the screen to open setup instead of no-op'ing.
                if (!selectAstronomical()) {
                    return flowOf(CalculationMethodResult.RequiresAstronomicalSetup)
                }
            is CalculationMethodAction.SetMethod -> updateAstronomical { it.copy(method = action.method) }
            is CalculationMethodAction.SetMazhab -> updateAstronomical { it.copy(mazhab = action.mazhab) }
            is CalculationMethodAction.SetAdjustments ->
                updateAstronomical { it.copy(adjustments = action.adjustments) }
            is CalculationMethodAction.SetCoordinates -> setCoordinates(action.coordinates)
        }
        val method = miqatRepository.getCalculationMethod()
        val preview = method.asAstronomical?.let {
            miqatRepository.previewMiqatData(timestampSecs = middayEpochSecs(), method = method)
        }
        return flowOf(CalculationMethodResult.Loaded(method = method, preview = preview))
    }

    /**
     * Restore the retained astronomical config, returning `true` if one existed and was applied.
     * `false` means the user has never configured astronomical mode.
     */
    private fun selectAstronomical(): Boolean {
        val retained = miqatRepository.getRetainedAstronomicalConfig() ?: return false
        miqatRepository.setCalculationMethod(MiqatCalculationMethod.Astronomical(retained))
        return true
    }

    /** Apply [transform] to the active-or-retained astronomical config and persist it. */
    private fun updateAstronomical(transform: (AstronomicalConfig) -> AstronomicalConfig) {
        val config = astronomicalConfig() ?: return
        miqatRepository.setCalculationMethod(
            MiqatCalculationMethod.Astronomical(transform(config))
        )
    }

    /**
     * Set coordinates, seeding a default config the first time a location is chosen so
     * astronomical mode becomes usable.
     */
    private fun setCoordinates(coordinates: Coordinates) {
        val updated = astronomicalConfig()?.copy(coordinates = coordinates)
            ?: AstronomicalConfig(
                coordinates = coordinates,
                method = AstronomicalMethod.Preset(Method.MUSLIM_WORLD_LEAGUE),
            )
        miqatRepository.setCalculationMethod(MiqatCalculationMethod.Astronomical(updated))
    }

    private fun astronomicalConfig(): AstronomicalConfig? =
        miqatRepository.getCalculationMethod().asAstronomical
            ?: miqatRepository.getRetainedAstronomicalConfig()
}

/**
 * Epoch seconds at 12:00 local time today. miqat keys by the day of the UTC timestamp; anchoring at
 * midday keeps a day near a UTC boundary resolving to the intended date. Mirrors the prayer-times
 * screen's `middayEpochSecs`.
 */
private fun middayEpochSecs(): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis / 1000
}
