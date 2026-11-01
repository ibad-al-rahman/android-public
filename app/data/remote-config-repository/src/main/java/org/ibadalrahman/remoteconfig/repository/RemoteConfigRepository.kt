package org.ibadalrahman.remoteconfig.repository

import kotlinx.coroutines.flow.StateFlow

interface RemoteConfigRepository {
    /**
     * Emits whether the app must force the user to update. Starts at `false` (the local default) and
     * flips when [refresh] activates a Remote Config where `ff_mobile_forceUpdate` is `true`.
     */
    val forceUpdateFlow: StateFlow<Boolean>

    /** Fetches and activates the latest Remote Config, then publishes the force-update flag. */
    suspend fun refresh()
}
