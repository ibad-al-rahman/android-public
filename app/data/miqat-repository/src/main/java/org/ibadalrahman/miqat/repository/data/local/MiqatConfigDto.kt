package org.ibadalrahman.miqat.repository.data.local

import org.ibadalrahman.miqat.Coordinates
import org.ibadalrahman.miqat.Mazhab
import org.ibadalrahman.miqat.Method
import org.ibadalrahman.miqat.Provider
import org.ibadalrahman.miqat.ProviderCity
import org.ibadalrahman.miqat.TimeAdjustment
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalConfig
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod

/**
 * Gson-friendly serialization layer for the calculation config.
 *
 * The library and domain types are sealed classes / enums that Gson cannot round-trip on its own.
 * These flat DTOs encode enums by stable string keys (mirroring the iOS Codable scheme) so the
 * persisted JSON does not depend on synthesized ordering and stays readable across versions.
 */
internal data class MiqatCalculationMethodDto(
    // Exactly one of these is non-null, tagging the variant.
    val astronomical: AstronomicalConfigDto? = null,
    val precomputed: ProviderDto? = null,
)

internal data class AstronomicalConfigDto(
    val coordinates: CoordinatesDto,
    val method: AstronomicalMethodDto,
    val mazhab: String,
    val adjustments: TimeAdjustmentDto,
)

internal data class AstronomicalMethodDto(
    // Exactly one of these is non-null, tagging the variant.
    val preset: String? = null,
    val custom: CustomAnglesDto? = null,
)

internal data class CustomAnglesDto(val fajrAngle: Double, val ishaaAngle: Double)

internal data class CoordinatesDto(val latitude: Double, val longitude: Double)

internal data class TimeAdjustmentDto(
    val fajr: Long,
    val sunrise: Long,
    val dhuhr: Long,
    val asr: Long,
    val maghrib: Long,
    val ishaa: Long,
)

internal data class ProviderDto(
    // Only Dar El-Fatwa exists today; the field carries the city.
    val darElFatwa: String? = null,
)

// region domain -> dto

internal fun MiqatCalculationMethod.toDto(): MiqatCalculationMethodDto = when (this) {
    is MiqatCalculationMethod.Astronomical ->
        MiqatCalculationMethodDto(astronomical = config.toDto())
    is MiqatCalculationMethod.Precomputed ->
        MiqatCalculationMethodDto(precomputed = provider.toDto())
}

internal fun AstronomicalConfig.toDto(): AstronomicalConfigDto = AstronomicalConfigDto(
    coordinates = CoordinatesDto(coordinates.latitude, coordinates.longitude),
    method = method.toDto(),
    mazhab = mazhab.key,
    adjustments = adjustments.toDto(),
)

private fun AstronomicalMethod.toDto(): AstronomicalMethodDto = when (this) {
    is AstronomicalMethod.Preset -> AstronomicalMethodDto(preset = method.key)
    is AstronomicalMethod.Custom ->
        AstronomicalMethodDto(custom = CustomAnglesDto(fajrAngle, ishaaAngle))
}

private fun TimeAdjustment.toDto(): TimeAdjustmentDto =
    TimeAdjustmentDto(fajr, sunrise, dhuhr, asr, maghrib, ishaa)

private fun Provider.toDto(): ProviderDto = when (this) {
    is Provider.DarElFatwa -> ProviderDto(darElFatwa = v1.key)
}

// endregion

// region dto -> domain

internal fun MiqatCalculationMethodDto.toDomain(): MiqatCalculationMethod = when {
    astronomical != null -> MiqatCalculationMethod.Astronomical(astronomical.toDomain())
    precomputed != null -> MiqatCalculationMethod.Precomputed(precomputed.toDomain())
    else -> error("Unknown MiqatCalculationMethod payload")
}

internal fun AstronomicalConfigDto.toDomain(): AstronomicalConfig = AstronomicalConfig(
    coordinates = Coordinates(coordinates.latitude, coordinates.longitude),
    method = method.toDomain(),
    mazhab = mazhabFromKey(mazhab),
    adjustments = adjustments.toDomain(),
)

private fun AstronomicalMethodDto.toDomain(): AstronomicalMethod = when {
    preset != null -> AstronomicalMethod.Preset(methodFromKey(preset))
    custom != null -> AstronomicalMethod.Custom(custom.fajrAngle, custom.ishaaAngle)
    else -> error("Unknown AstronomicalMethod payload")
}

private fun TimeAdjustmentDto.toDomain(): TimeAdjustment =
    TimeAdjustment(fajr, sunrise, dhuhr, asr, maghrib, ishaa)

private fun ProviderDto.toDomain(): Provider = when {
    darElFatwa != null -> Provider.DarElFatwa(providerCityFromKey(darElFatwa))
    else -> error("Unknown Provider payload")
}

// endregion

// region stable enum keys

private val Method.key: String
    get() = when (this) {
        Method.MUSLIM_WORLD_LEAGUE -> "muslimWorldLeague"
        Method.EGYPTIAN -> "egyptian"
        Method.UMM_AL_QURA -> "ummAlQura"
        Method.MOONSIGHTING_COMMITTEE -> "moonsightingCommittee"
        Method.NORTH_AMERICA -> "northAmerica"
        Method.SINGAPORE -> "singapore"
    }

private fun methodFromKey(key: String): Method = when (key) {
    "muslimWorldLeague" -> Method.MUSLIM_WORLD_LEAGUE
    "egyptian" -> Method.EGYPTIAN
    "ummAlQura" -> Method.UMM_AL_QURA
    "moonsightingCommittee" -> Method.MOONSIGHTING_COMMITTEE
    "northAmerica" -> Method.NORTH_AMERICA
    "singapore" -> Method.SINGAPORE
    else -> error("Unknown Method: $key")
}

private val Mazhab.key: String
    get() = when (this) {
        Mazhab.SHAFI -> "shafi"
        Mazhab.HANAFI -> "hanafi"
    }

private fun mazhabFromKey(key: String): Mazhab = when (key) {
    "shafi" -> Mazhab.SHAFI
    "hanafi" -> Mazhab.HANAFI
    else -> error("Unknown Mazhab: $key")
}

private val ProviderCity.key: String
    get() = when (this) {
        ProviderCity.BEIRUT -> "beirut"
    }

private fun providerCityFromKey(key: String): ProviderCity = when (key) {
    "beirut" -> ProviderCity.BEIRUT
    else -> error("Unknown ProviderCity: $key")
}

// endregion
