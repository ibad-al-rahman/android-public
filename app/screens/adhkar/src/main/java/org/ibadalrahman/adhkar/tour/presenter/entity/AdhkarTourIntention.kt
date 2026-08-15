package org.ibadalrahman.adhkar.tour.presenter.entity

sealed interface AdhkarTourIntention {
    /** Fired on screen start to load the collection. */
    data object Load : AdhkarTourIntention

    /** The user tapped the active dhikr area. */
    data object Tapped : AdhkarTourIntention

    /** Swipe/button forward. */
    data object Next : AdhkarTourIntention

    /** Swipe/button back. */
    data object Previous : AdhkarTourIntention

    /** The user tapped the completion screen to leave the tour. */
    data object Finish : AdhkarTourIntention
}
