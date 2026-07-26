package org.ibadalrahman.miqat.repository.data.local

import org.ibadalrahman.miqat.repository.data.domain.AstronomicalConfig
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod

/**
 * Persists the miqat calculation configuration. Mirrors the self-contained storage in iOS
 * `MiqatSharedStorage`, but backed by the module's own [android.content.SharedPreferences].
 */
interface MiqatLocalDataSource {
    /** The persisted method, or the [MiqatCalculationMethod.default] when nothing is stored. */
    fun getCalculationMethod(): MiqatCalculationMethod

    fun saveCalculationMethod(method: MiqatCalculationMethod)

    /**
     * The last astronomical config the user set, retained across a toggle to precomputed and back.
     * `null` until the user has ever configured an astronomical method.
     */
    fun getRetainedAstronomicalConfig(): AstronomicalConfig?

    fun saveRetainedAstronomicalConfig(config: AstronomicalConfig)
}
