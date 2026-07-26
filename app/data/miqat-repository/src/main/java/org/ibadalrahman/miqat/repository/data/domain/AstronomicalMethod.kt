package org.ibadalrahman.miqat.repository.data.domain

import org.ibadalrahman.miqat.Method

/**
 * The astronomical method: one of the library presets, or a custom pair of twilight angles.
 *
 * Port of iOS `AstronomicalMethod`.
 */
sealed interface AstronomicalMethod {
    data class Preset(val method: Method) : AstronomicalMethod
    data class Custom(val fajrAngle: Double, val ishaaAngle: Double) : AstronomicalMethod
}
