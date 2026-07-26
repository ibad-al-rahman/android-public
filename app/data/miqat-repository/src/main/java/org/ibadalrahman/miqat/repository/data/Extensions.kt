package org.ibadalrahman.miqat.repository.data

import org.ibadalrahman.miqat.CalculationParameters
import org.ibadalrahman.miqat.HighLatitudeRule
import org.ibadalrahman.miqat.Mazhab
import org.ibadalrahman.miqat.PrayerTimes
import org.ibadalrahman.miqat.Rounding
import org.ibadalrahman.miqat.parametersForMethod
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalConfig
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.miqat.repository.data.domain.zero

/**
 * Computes the library [PrayerTimes] for [timestampSecs] using [method].
 *
 * The caller owns the returned handle: [PrayerTimes] is `AutoCloseable`, so read from it inside a
 * `use { }` block and let it close.
 */
internal fun MiqatCalculationMethod.prayerTimes(timestampSecs: Long): PrayerTimes = when (this) {
    is MiqatCalculationMethod.Precomputed ->
        PrayerTimes.fromPrecomputed(dateUtcTimestampSecs = timestampSecs, provider = provider)

    is MiqatCalculationMethod.Astronomical ->
        astronomicalPrayerTimes(timestampSecs = timestampSecs, config = config)
}

/**
 * Computes astronomical prayer times for a config.
 *
 * A preset method with no user customization goes through `fromMethod` so its exact library
 * behavior is preserved — notably Umm al-Qura's interval-based Ishaa, which the flat
 * [CalculationParameters] view collapses to a `0°` angle. Any custom method, non-Shafi madhab, or
 * non-zero offset requires the parameter-based path.
 *
 * Port of iOS `MiqatData.astronomicalPrayerTimes`.
 */
private fun astronomicalPrayerTimes(
    timestampSecs: Long,
    config: AstronomicalConfig,
): PrayerTimes {
    val method = config.method
    if (method is AstronomicalMethod.Preset &&
        config.mazhab == Mazhab.SHAFI &&
        config.adjustments == TimeAdjustmentZero
    ) {
        return PrayerTimes.fromMethod(
            dateUtcTimestampSecs = timestampSecs,
            coordinates = config.coordinates,
            method = method.method,
        )
    }

    val base: CalculationParameters = when (method) {
        is AstronomicalMethod.Preset -> parametersForMethod(method.method)
        is AstronomicalMethod.Custom -> CalculationParameters(
            fajrAngle = method.fajrAngle,
            ishaaAngle = method.ishaaAngle,
            mazhab = Mazhab.SHAFI,
            highLatitudeRule = HighLatitudeRule.MIDDLE_OF_THE_NIGHT,
            adjustments = TimeAdjustmentZero,
            methodAdjustments = TimeAdjustmentZero,
            rounding = Rounding.NEAREST,
        )
    }

    val parameters = CalculationParameters(
        fajrAngle = base.fajrAngle,
        ishaaAngle = base.ishaaAngle,
        mazhab = config.mazhab,
        highLatitudeRule = base.highLatitudeRule,
        adjustments = config.adjustments,
        methodAdjustments = base.methodAdjustments,
        rounding = base.rounding,
    )

    return PrayerTimes.fromParameters(
        dateUtcTimestampSecs = timestampSecs,
        coordinates = config.coordinates,
        parameters = parameters,
    )
}

private val TimeAdjustmentZero = org.ibadalrahman.miqat.TimeAdjustment.zero
