package org.ibadalrahman.remoteconfig.repository.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ibadalrahman.remoteconfig.repository.BuildConfig
import org.ibadalrahman.remoteconfig.repository.data.domain.FeatureFlagKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {

    @Provides
    @Singleton
    fun providesFirebaseRemoteConfig(): FirebaseRemoteConfig =
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    // Debug fetches fresh every launch so the flag is easy to test; release throttles
                    // to once an hour to respect Firebase fetch limits.
                    minimumFetchIntervalInSeconds =
                        if (BuildConfig.DEBUG) 0L else RELEASE_FETCH_INTERVAL_SECONDS
                }
            )
            // Local defaults keep the app safe before the Firebase parameter exists / before the
            // first successful fetch: force update is off.
            setDefaultsAsync(mapOf(FeatureFlagKey.ForceUpdate.key to false))
        }

    private const val RELEASE_FETCH_INTERVAL_SECONDS = 3600L
}
