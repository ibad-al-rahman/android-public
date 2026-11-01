package org.ibadalrahman.remoteconfig.repository.data.remote

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import org.ibadalrahman.remoteconfig.repository.data.domain.FeatureFlagKey
import org.ibadalrahman.tracker.Tracker
import javax.inject.Inject

class RemoteConfigDataSourceImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
    private val tracker: Tracker,
) : RemoteConfigDataSource {

    override suspend fun fetchFlag(key: FeatureFlagKey): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
            remoteConfig.getBoolean(key.key)
        } catch (error: Exception) {
            // Offline or Firebase error: fall back to the last activated value (or the local
            // default of false). Never surface the failure — a broken fetch must not block the app,
            // but do record it as a non-fatal so we can see fetch health in Crashlytics.
            Log.w(TAG, "Remote Config fetch failed for ${key.key}; using last value", error)
            tracker.recordException(error)
            remoteConfig.getBoolean(key.key)
        }
    }

    private companion object {
        const val TAG = "RemoteConfigDataSource"
    }
}
