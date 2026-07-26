package org.ibadalrahman.miqat.repository.data.local

import com.google.gson.Gson
import org.ibadalrahman.miqat.Coordinates
import org.ibadalrahman.miqat.Mazhab
import org.ibadalrahman.miqat.Method
import org.ibadalrahman.miqat.Provider
import org.ibadalrahman.miqat.ProviderCity
import org.ibadalrahman.miqat.TimeAdjustment
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalConfig
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pure-JVM round-trip tests for the config serialization layer. These do not touch the miqat
 * native library, so they run as ordinary unit tests.
 */
class MiqatConfigDtoTest {

    private val gson = Gson()

    private fun roundTrip(method: MiqatCalculationMethod): MiqatCalculationMethod {
        val json = gson.toJson(method.toDto())
        return gson.fromJson(json, MiqatCalculationMethodDto::class.java).toDomain()
    }

    @Test
    fun `precomputed method round-trips`() {
        val method = MiqatCalculationMethod.Precomputed(Provider.DarElFatwa(ProviderCity.BEIRUT))
        assertEquals(method, roundTrip(method))
    }

    @Test
    fun `default method round-trips`() {
        assertEquals(MiqatCalculationMethod.default, roundTrip(MiqatCalculationMethod.default))
    }

    @Test
    fun `astronomical preset method round-trips`() {
        val method = MiqatCalculationMethod.Astronomical(
            AstronomicalConfig(
                coordinates = Coordinates(latitude = 33.8938, longitude = 35.5018),
                method = AstronomicalMethod.Preset(Method.UMM_AL_QURA),
                mazhab = Mazhab.HANAFI,
                adjustments = TimeAdjustment(1, -2, 3, -4, 5, -6),
            ),
        )
        assertEquals(method, roundTrip(method))
    }

    @Test
    fun `astronomical custom angles round-trip`() {
        val method = MiqatCalculationMethod.Astronomical(
            AstronomicalConfig(
                coordinates = Coordinates(latitude = 51.5074, longitude = -0.1278),
                method = AstronomicalMethod.Custom(fajrAngle = 18.0, ishaaAngle = 17.5),
            ),
        )
        assertEquals(method, roundTrip(method))
    }

    @Test
    fun `enum keys are stable strings not ordinals`() {
        val method = MiqatCalculationMethod.Astronomical(
            AstronomicalConfig(
                coordinates = Coordinates(latitude = 0.0, longitude = 0.0),
                method = AstronomicalMethod.Preset(Method.MOONSIGHTING_COMMITTEE),
                mazhab = Mazhab.SHAFI,
            ),
        )
        val json = gson.toJson(method.toDto())
        assert(json.contains("moonsightingCommittee")) { "expected stable method key in $json" }
        assert(json.contains("shafi")) { "expected stable mazhab key in $json" }
    }
}
