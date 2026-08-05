package org.ibadalrahman.services.notifications

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NextEventSelectorTest {

    private val minute = 60_000L
    private val hour = 60 * minute

    @Test
    fun `selects the earliest event strictly after now`() {
        val now = 1_000_000L
        val candidates = listOf(
            NotificationEvent(NotificationEventType.FAJR, now - hour),      // past
            NotificationEvent(NotificationEventType.DHUHR, now + 2 * hour),
            NotificationEvent(NotificationEventType.ASR, now + hour),       // earliest future
        )

        val batch = NextEventSelector.selectNextBatch(candidates, now)

        assertEquals(1, batch.size)
        assertEquals(NotificationEventType.ASR, batch.first().type)
    }

    @Test
    fun `an event exactly at now is not eligible`() {
        val now = 1_000_000L
        val candidates = listOf(
            NotificationEvent(NotificationEventType.FAJR, now),             // strictly-after excludes this
            NotificationEvent(NotificationEventType.DHUHR, now + hour),
        )

        val batch = NextEventSelector.selectNextBatch(candidates, now)

        assertEquals(NotificationEventType.DHUHR, batch.single().type)
    }

    @Test
    fun `coalesces events within the 60s window into one batch`() {
        val now = 1_000_000L
        val maghrib = now + hour
        val candidates = listOf(
            NotificationEvent(NotificationEventType.MAGHRIB, maghrib),
            NotificationEvent(NotificationEventType.EVENING_ADHKAR, maghrib + 30_000L), // within 60s
            NotificationEvent(NotificationEventType.ISHAA, maghrib + 2 * hour),
        )

        val batch = NextEventSelector.selectNextBatch(candidates, now)

        assertEquals(2, batch.size)
        assertEquals(
            listOf(NotificationEventType.MAGHRIB, NotificationEventType.EVENING_ADHKAR),
            batch.map { it.type },
        )
    }

    @Test
    fun `does not coalesce events more than 60s apart`() {
        val now = 1_000_000L
        val first = now + hour
        val candidates = listOf(
            NotificationEvent(NotificationEventType.MAGHRIB, first),
            NotificationEvent(NotificationEventType.EVENING_ADHKAR, first + 61_000L), // outside window
        )

        val batch = NextEventSelector.selectNextBatch(candidates, now)

        assertEquals(1, batch.size)
        assertEquals(NotificationEventType.MAGHRIB, batch.single().type)
    }

    @Test
    fun `returns empty when no candidates are in the future`() {
        val now = 1_000_000L
        val candidates = listOf(
            NotificationEvent(NotificationEventType.FAJR, now - hour),
            NotificationEvent(NotificationEventType.ISHAA, now - minute),
        )

        val batch = NextEventSelector.selectNextBatch(candidates, now)

        assertTrue(batch.isEmpty())
    }

    @Test
    fun `returns empty for no candidates at all`() {
        assertTrue(NextEventSelector.selectNextBatch(emptyList(), 1_000_000L).isEmpty())
    }
}
