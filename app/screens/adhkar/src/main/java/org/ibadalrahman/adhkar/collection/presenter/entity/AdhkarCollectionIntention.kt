package org.ibadalrahman.adhkar.collection.presenter.entity

import org.ibadalrahman.adhkar.domain.entity.AdhkarCollection

sealed interface AdhkarCollectionIntention {
    data class CollectionTapped(val collection: AdhkarCollection) : AdhkarCollectionIntention
}
