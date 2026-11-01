package org.ibadalrahman.remoteconfig.repository.data.remote

import org.ibadalrahman.remoteconfig.repository.data.domain.FeatureFlagKey

interface RemoteConfigDataSource {
    /**
     * Fetches and activates the latest Remote Config, then returns the boolean value for [key].
     * Returns the local default (`false`) if the fetch fails.
     */
    suspend fun fetchFlag(key: FeatureFlagKey): Boolean
}
