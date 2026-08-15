package org.ibadalrahman.adhkar.tour.presenter.entity

sealed interface AdhkarTourViewAction {
    /** Leave the tour, returning to the collection list. */
    data object Close : AdhkarTourViewAction
}
