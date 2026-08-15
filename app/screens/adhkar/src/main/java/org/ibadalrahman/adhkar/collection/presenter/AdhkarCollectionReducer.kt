package org.ibadalrahman.adhkar.collection.presenter

import org.ibadalrahman.adhkar.collection.domain.entity.AdhkarCollectionResult
import org.ibadalrahman.adhkar.collection.presenter.entity.AdhkarCollectionScreenState

/**
 * The collection list has no domain results, so there is nothing to reduce — the state is a static
 * list. Present for parity with the MVI pattern.
 */
object AdhkarCollectionReducer {
    fun reduce(
        prevState: AdhkarCollectionScreenState,
        result: AdhkarCollectionResult,
    ): AdhkarCollectionScreenState = prevState
}
