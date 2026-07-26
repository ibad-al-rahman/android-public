package org.ibadalrahman.miqat.repository.data.domain

import org.ibadalrahman.miqat.Provider
import org.ibadalrahman.miqat.ProviderCity

/**
 * The persisted, shareable prayer-times calculation method.
 *
 * This is the "provider" that [org.ibadalrahman.miqat.repository.MiqatRepository] reads on every
 * `getMiqatData` call. The astronomical case carries its own coordinates so it can be fully
 * recomputed without reaching into app-only state.
 *
 * Port of iOS `MiqatPrayerTimesCalculationMethod`.
 */
sealed interface MiqatCalculationMethod {
    data class Astronomical(val config: AstronomicalConfig) : MiqatCalculationMethod
    data class Precomputed(val provider: Provider) : MiqatCalculationMethod

    /**
     * The astronomical configuration, or `null` when the current method is precomputed.
     *
     * Convenience for screens that edit one slice of the config: read this, mutate a field, and
     * write the whole method back via `MiqatRepository.setCalculationMethod`.
     */
    val asAstronomical: AstronomicalConfig?
        get() = (this as? Astronomical)?.config

    companion object {
        /** Default used when nothing has been selected yet. */
        val default: MiqatCalculationMethod =
            Precomputed(Provider.DarElFatwa(ProviderCity.BEIRUT))
    }
}
