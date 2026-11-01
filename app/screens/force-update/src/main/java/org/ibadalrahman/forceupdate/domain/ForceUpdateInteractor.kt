package org.ibadalrahman.forceupdate.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.ibadalrahman.forceupdate.domain.entity.ForceUpdateAction
import org.ibadalrahman.forceupdate.domain.entity.ForceUpdateResult
import org.ibadalrahman.mvi.BaseInteractor
import org.ibadalrahman.tracker.Tracker
import org.ibadalrahman.tracker.events.ForceUpdateEvents
import javax.inject.Inject

/**
 * The force-update screen only signals intent — the actual Play In-App Update flow is owned by the
 * host Activity (which `startUpdateFlowForResult` requires). Tapping update maps straight to
 * [ForceUpdateResult.UpdateRequested], which the ViewModel turns into a one-shot view action.
 * Both the "shown" and "tapped" moments are logged to analytics here.
 */
class ForceUpdateInteractor @Inject constructor(
    private val tracker: Tracker,
) : BaseInteractor<ForceUpdateAction, ForceUpdateResult> {

    override suspend fun resultFrom(action: ForceUpdateAction): Flow<ForceUpdateResult> =
        when (action) {
            ForceUpdateAction.TrackShown -> {
                tracker.trackEvent(ForceUpdateEvents.Shown)
                emptyFlow()
            }
            ForceUpdateAction.RequestUpdate -> {
                tracker.trackEvent(ForceUpdateEvents.UpdateTapped)
                flowOf(ForceUpdateResult.UpdateRequested)
            }
        }
}
