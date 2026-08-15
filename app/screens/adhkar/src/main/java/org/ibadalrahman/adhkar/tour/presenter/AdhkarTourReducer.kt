package org.ibadalrahman.adhkar.tour.presenter

import org.ibadalrahman.adhkar.tour.domain.entity.AdhkarTourResult
import org.ibadalrahman.adhkar.tour.presenter.entity.AdhkarTourScreenState

/**
 * Pure state machine for the tour. Mirrors the iOS reducer:
 * - [AdhkarTourResult.Tapped] counts up while the active dhikr is incomplete, then advances once it
 *   is complete.
 * - [AdhkarTourResult.Next] advances (past the last dhikr sets `activeIndex = null`, the completion
 *   screen).
 * - [AdhkarTourResult.Previous] moves back one, clamped so it never leaves the tour.
 *
 * Counts persist across navigation.
 */
object AdhkarTourReducer {
    fun reduce(
        prevState: AdhkarTourScreenState,
        result: AdhkarTourResult,
    ): AdhkarTourScreenState = when (result) {
        is AdhkarTourResult.Loaded -> {
            val dhikr = result.collection.adhkar
            AdhkarTourScreenState(
                collection = result.collection,
                dhikr = dhikr,
                counts = List(dhikr.size) { 0 },
                activeIndex = if (dhikr.isEmpty()) null else 0,
            )
        }

        AdhkarTourResult.Tapped ->
            if (prevState.isActiveComplete) prevState.advance() else prevState.increment()

        AdhkarTourResult.Next -> prevState.advance()

        AdhkarTourResult.Previous -> prevState.moveActive(-1)
    }

    /** Increment the active dhikr's count. No-op when finished. */
    private fun AdhkarTourScreenState.increment(): AdhkarTourScreenState {
        val index = activeIndex ?: return this
        val counts = counts.toMutableList().also { it[index] = it[index] + 1 }
        return copy(counts = counts)
    }

    /** Move to the next dhikr, or `null` past the last one (which shows completion). */
    private fun AdhkarTourScreenState.advance(): AdhkarTourScreenState {
        val index = activeIndex ?: return this
        val next = index + 1
        return copy(activeIndex = if (next in dhikr.indices) next else null)
    }

    /** Clamped navigation used by swipe; never sets `activeIndex` to `null`. */
    private fun AdhkarTourScreenState.moveActive(offset: Int): AdhkarTourScreenState {
        val index = activeIndex ?: return this
        val target = index + offset
        return if (target in dhikr.indices) copy(activeIndex = target) else this
    }
}
