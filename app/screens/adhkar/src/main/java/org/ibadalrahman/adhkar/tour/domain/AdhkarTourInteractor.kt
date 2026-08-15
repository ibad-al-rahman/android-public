package org.ibadalrahman.adhkar.tour.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.ibadalrahman.adhkar.domain.entity.AdhkarCollection
import org.ibadalrahman.adhkar.tour.domain.entity.AdhkarTourAction
import org.ibadalrahman.adhkar.tour.domain.entity.AdhkarTourResult
import org.ibadalrahman.mvi.BaseInteractor
import javax.inject.Inject

/**
 * Resolves the toured collection from its slug. All other tour behaviour (counting, advancing) is
 * pure in-memory state handled by the reducer via inline results, so this interactor only serves
 * [AdhkarTourAction.Load].
 */
class AdhkarTourInteractor @Inject constructor() :
    BaseInteractor<AdhkarTourAction, AdhkarTourResult> {

    override suspend fun resultFrom(action: AdhkarTourAction): Flow<AdhkarTourResult> =
        when (action) {
            is AdhkarTourAction.Load ->
                AdhkarCollection.fromSlug(action.slug)
                    ?.let { flowOf(AdhkarTourResult.Loaded(it)) }
                    ?: emptyFlow()
        }
}
