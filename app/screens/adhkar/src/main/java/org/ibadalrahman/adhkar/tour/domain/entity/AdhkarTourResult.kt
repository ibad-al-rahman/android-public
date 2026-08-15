package org.ibadalrahman.adhkar.tour.domain.entity

import org.ibadalrahman.adhkar.domain.entity.AdhkarCollection

sealed interface AdhkarTourResult {
    /** The collection was resolved; build a fresh tour (counts zeroed, first dhikr active). */
    data class Loaded(val collection: AdhkarCollection) : AdhkarTourResult

    /** The user tapped the active dhikr: count it, or advance if it is already complete. */
    data object Tapped : AdhkarTourResult

    /** Advance to the next dhikr (or the completion screen past the last one). */
    data object Next : AdhkarTourResult

    /** Move back to the previous dhikr (clamped; never leaves the tour). */
    data object Previous : AdhkarTourResult
}
