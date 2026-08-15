package org.ibadalrahman.adhkar.collection.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.ibadalrahman.adhkar.collection.domain.entity.AdhkarCollectionAction
import org.ibadalrahman.adhkar.collection.domain.entity.AdhkarCollectionResult
import org.ibadalrahman.mvi.BaseInteractor
import javax.inject.Inject

/**
 * No-op interactor: the collection list has no domain logic (the list is static and picking a
 * collection is pure navigation). Present only to satisfy [BaseInteractor].
 */
class AdhkarCollectionInteractor @Inject constructor() :
    BaseInteractor<AdhkarCollectionAction, AdhkarCollectionResult> {

    override suspend fun resultFrom(action: AdhkarCollectionAction): Flow<AdhkarCollectionResult> =
        emptyFlow()
}
