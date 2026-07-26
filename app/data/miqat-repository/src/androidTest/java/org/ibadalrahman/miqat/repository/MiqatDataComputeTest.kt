package org.ibadalrahman.miqat.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.ibadalrahman.miqat.Provider
import org.ibadalrahman.miqat.ProviderCity
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.miqat.repository.data.domain.MiqatData
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test: exercises the miqat native library, which is only available on a device or
 * emulator (the AAR ships `.so` binaries).
 */
@RunWith(AndroidJUnit4::class)
class MiqatDataComputeTest {

    // 2026-07-26 12:00 UTC — an ordinary (non-Ramadan, non-Eid) day.
    private val timestampSecs = 1_784_635_200L

    private val beirut = MiqatCalculationMethod.Precomputed(Provider.DarElFatwa(ProviderCity.BEIRUT))

    @Test
    fun precomputedBeirutProducesOrderedPrayerTimes() {
        val data: MiqatData = MiqatData.compute(timestampSecs, beirut)

        val ordered = listOf(data.fajr, data.sunrise, data.dhuhr, data.asr, data.maghrib, data.ishaa)
        ordered.zipWithNext { earlier, later ->
            assertTrue(
                "expected $earlier before $later",
                earlier.time < later.time,
            )
        }
    }

    @Test
    fun ordinaryDayHasNoImsakOrEid() {
        val data = MiqatData.compute(timestampSecs, beirut)
        assertNull(data.imsak)
        assertNull(data.eid)
    }

    @Test
    fun idIsFormattedYearMonthDay() {
        val data = MiqatData.compute(timestampSecs, beirut)
        assertTrue("id should be 8 digits, was ${data.id}", Regex("""\d{8}""").matches(data.id))
    }
}
