package org.ibadalrahman.adhkar.tour

import org.ibadalrahman.adhkar.domain.entity.AdhkarCollection
import org.ibadalrahman.adhkar.tour.domain.entity.AdhkarTourResult
import org.ibadalrahman.adhkar.tour.presenter.AdhkarTourReducer
import org.ibadalrahman.adhkar.tour.presenter.entity.AdhkarTourScreenState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AdhkarTourReducerTest {

    private val collection = AdhkarCollection.Morning

    private fun loaded(): AdhkarTourScreenState = AdhkarTourReducer.reduce(
        prevState = AdhkarTourScreenState.Empty,
        result = AdhkarTourResult.Loaded(collection),
    )

    private fun AdhkarTourScreenState.reduce(result: AdhkarTourResult) =
        AdhkarTourReducer.reduce(prevState = this, result = result)

    @Test
    fun `Loaded builds zeroed counts with the first dhikr active`() {
        val state = loaded()

        assertEquals(collection, state.collection)
        assertEquals(collection.adhkar, state.dhikr)
        assertEquals(collection.adhkar.size, state.counts.size)
        assertTrue(state.counts.all { it == 0 })
        assertEquals(0, state.activeIndex)
        assertEquals(1, state.position)
    }

    @Test
    fun `Tapped on an incomplete dhikr increments only its own count`() {
        // Second dhikr (index 1) has target 3, so the first tap only counts.
        val state = loaded().reduce(AdhkarTourResult.Next)

        val tapped = state.reduce(AdhkarTourResult.Tapped)

        assertEquals(1, tapped.activeIndex)
        assertEquals(1, tapped.counts[1])
        assertEquals(0, tapped.counts[0])
        assertFalse(tapped.isActiveComplete)
    }

    @Test
    fun `Tapped on a complete dhikr advances to the next one`() {
        // First dhikr (index 0) has target 1, so one tap completes it.
        val completed = loaded().reduce(AdhkarTourResult.Tapped)
        assertTrue(completed.isActiveComplete)

        val advanced = completed.reduce(AdhkarTourResult.Tapped)

        assertEquals(1, advanced.activeIndex)
        // The first dhikr's count persists after advancing.
        assertEquals(1, advanced.counts[0])
    }

    @Test
    fun `Tapping through the last complete dhikr finishes the tour`() {
        // Jump to the last dhikr and complete it.
        var state = loaded()
        repeat(collection.adhkar.lastIndex) { state = state.reduce(AdhkarTourResult.Next) }
        assertEquals(collection.adhkar.lastIndex, state.activeIndex)

        repeat(collection.adhkar.last().target) { state = state.reduce(AdhkarTourResult.Tapped) }
        assertTrue(state.isActiveComplete)

        val finished = state.reduce(AdhkarTourResult.Tapped)

        assertNull(finished.activeIndex)
        assertTrue(finished.isFinished)
        assertEquals(1f, finished.progress, 0f)
    }

    @Test
    fun `Previous clamps at the first dhikr and never leaves the tour`() {
        val state = loaded()

        val back = state.reduce(AdhkarTourResult.Previous)

        assertEquals(0, back.activeIndex)
    }

    @Test
    fun `counts persist across Next and Previous`() {
        // Count the first dhikr partially is not possible (target 1); use the second (target 3).
        var state = loaded().reduce(AdhkarTourResult.Next)
        state = state.reduce(AdhkarTourResult.Tapped)
        state = state.reduce(AdhkarTourResult.Tapped)
        assertEquals(2, state.counts[1])

        state = state.reduce(AdhkarTourResult.Previous)
        state = state.reduce(AdhkarTourResult.Next)

        assertEquals(1, state.activeIndex)
        assertEquals(2, state.counts[1])
    }

    @Test
    fun `position and progress reflect the active index`() {
        val state = loaded().reduce(AdhkarTourResult.Next).reduce(AdhkarTourResult.Next)

        assertEquals(2, state.activeIndex)
        assertEquals(3, state.position)
        assertEquals(2f / collection.adhkar.size, state.progress, 0.0001f)
    }
}
