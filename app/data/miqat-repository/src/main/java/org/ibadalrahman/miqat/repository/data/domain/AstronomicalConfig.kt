package org.ibadalrahman.miqat.repository.data.domain

import org.ibadalrahman.miqat.Coordinates
import org.ibadalrahman.miqat.Mazhab
import org.ibadalrahman.miqat.TimeAdjustment

/**
 * The full, self-describing configuration for an astronomical calculation.
 *
 * It carries everything needed to recompute prayer times from scratch — coordinates, the method
 * (a preset or a user-defined pair of twilight angles), the Asr madhab, and per-prayer minute
 * offsets — so a widget could recompute without reaching into app-only state.
 *
 * Port of iOS `AstronomicalConfig`.
 */
data class AstronomicalConfig(
    val coordinates: Coordinates,
    val method: AstronomicalMethod,
    val mazhab: Mazhab = Mazhab.SHAFI,
    /** User-supplied per-prayer offsets, in minutes. */
    val adjustments: TimeAdjustment = TimeAdjustment.zero,
)

/** No offset on any prayer. */
val TimeAdjustment.Companion.zero: TimeAdjustment
    get() = TimeAdjustment(fajr = 0, sunrise = 0, dhuhr = 0, asr = 0, maghrib = 0, ishaa = 0)
