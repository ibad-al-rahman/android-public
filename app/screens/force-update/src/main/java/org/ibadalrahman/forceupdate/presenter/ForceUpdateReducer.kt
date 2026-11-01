package org.ibadalrahman.forceupdate.presenter

import org.ibadalrahman.forceupdate.domain.entity.ForceUpdateResult
import org.ibadalrahman.forceupdate.presenter.entity.ForceUpdateScreenState

object ForceUpdateReducer {
    fun reduce(
        prevState: ForceUpdateScreenState,
        result: ForceUpdateResult,
    ): ForceUpdateScreenState = when (result) {
        ForceUpdateResult.UpdateRequested -> prevState.copy(isStartingUpdate = true)
    }
}
