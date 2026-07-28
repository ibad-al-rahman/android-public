package org.ibadalrahman.settings.calculationmethod.domain

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
            CalculationMethodAction.SelectAstronomical -> selectAstronomical()
            is CalculationMethodAction.SetMethod -> updateAstronomical { it.copy(method = action.method) }
            is CalculationMethodAction.SetMazhab -> updateAstronomical { it.copy(mazhab = action.mazhab) }
            is CalculationMethodAction.SetAdjustments ->
                updateAstronomical { it.copy(adjustments = action.adjustments) }
            is CalculationMethodAction.SetCoordinates -> setCoordinates(action.coordinates)
        }
        return flowOf(CalculationMethodResult.Loaded(miqatRepository.getCalculationMethod()))
    }

    /** Restore the retained astronomical config, if the user ever configured one. */
    private fun selectAstronomical() {
        val retained = miqatRepository.getRetainedAstronomicalConfig() ?: return
        miqatRepository.setCalculationMethod(MiqatCalculationMethod.Astronomical(retained))
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
