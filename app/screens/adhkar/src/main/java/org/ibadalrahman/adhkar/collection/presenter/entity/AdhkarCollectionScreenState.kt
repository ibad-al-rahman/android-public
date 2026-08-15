package org.ibadalrahman.adhkar.collection.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.ibadalrahman.adhkar.domain.entity.AdhkarCollection

@Stable
@Immutable
data class AdhkarCollectionScreenState(
    val collections: List<AdhkarCollection>,
) {
    companion object {
        val Empty = AdhkarCollectionScreenState(collections = AdhkarCollection.entries)
    }
}
