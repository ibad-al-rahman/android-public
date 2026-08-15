package org.ibadalrahman.adhkar.tour.domain.entity

sealed interface AdhkarTourAction {
    /** Load the collection identified by [slug] and build the initial tour state. */
    data class Load(val slug: String?) : AdhkarTourAction
}
