package org.ibadalrahman.adhkar.collection.presenter.entity

import org.ibadalrahman.adhkar.domain.entity.AdhkarCollection

sealed interface AdhkarCollectionViewAction {
    /** Open the tour for the picked [collection]. */
    data class OpenTour(val collection: AdhkarCollection) : AdhkarCollectionViewAction
}
