package org.ibadalrahman.miqat.repository

import org.ibadalrahman.miqat.repository.data.domain.AstronomicalConfig
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatData
import org.ibadalrahman.miqat.repository.data.domain.MiqatEventOccurrence

/**
 * Computes Islamic prayer times locally with the miqat library. Port of iOS `MiqatService`.
 *
 * Calculation happens in-process with no I/O, so the methods are synchronous. The persisted
 * [MiqatCalculationMethod] selects between an astronomical calculation and a precomputed provider.
 */
interface MiqatRepository {
    /** Prayer times for the day of [timestampSecs] using the persisted calculation method. */
    fun getMiqatData(timestampSecs: Long): MiqatData

    /** Computes prayer times for an explicit [method] without reading or writing persistence. */
    fun previewMiqatData(timestampSecs: Long, method: MiqatCalculationMethod): MiqatData

    /** All recurring Islamic event occurrences in [gregorianYear]. */
    fun getIslamicEvents(gregorianYear: Int): List<MiqatEventOccurrence>

    /**
     * Persists [method]. When it is astronomical, its config is also retained separately so it can
     * be restored after toggling to precomputed and back (see [getRetainedAstronomicalConfig]).
     */
    fun setCalculationMethod(method: MiqatCalculationMethod)

    /** The currently persisted calculation method (defaults when none is stored). */
    fun getCalculationMethod(): MiqatCalculationMethod

    /**
     * The last astronomical config the user set, retained across a toggle to precomputed.
     * `null` if the user has never configured an astronomical method.
     */
    fun getRetainedAstronomicalConfig(): AstronomicalConfig?
}
