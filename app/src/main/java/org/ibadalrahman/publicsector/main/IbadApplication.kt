package org.ibadalrahman.publicsector.main

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.ibadalrahman.base.CoroutineDispatchers
import org.ibadalrahman.remoteconfig.repository.RemoteConfigRepository
import org.ibadalrahman.services.notifications.NotificationPoster
import org.ibadalrahman.services.notifications.NotificationRearmObserver
import javax.inject.Inject

@HiltAndroidApp
class IbadApplication: Application() {

    @Inject lateinit var notificationPoster: NotificationPoster
    @Inject lateinit var notificationRearmObserver: NotificationRearmObserver
    @Inject lateinit var remoteConfigRepository: RemoteConfigRepository
    @Inject lateinit var coroutineDispatchers: CoroutineDispatchers

    private val appScope by lazy {
        CoroutineScope(SupervisorJob() + coroutineDispatchers.io)
    }

    override fun onCreate() {
        super.onCreate()
        System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

        notificationPoster.ensureChannels()
        notificationRearmObserver.start()

        // Refresh Remote Config on every app foreground (ON_START also fires on the first launch),
        // so a force-update flipped on in Firebase reaches an already-running app on its next
        // foreground instead of only on a cold start. Firebase's own minimumFetchInterval still
        // throttles the actual network call, so this can't hammer the backend.
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    appScope.launch { remoteConfigRepository.refresh() }
                }
            }
        )
    }
}
