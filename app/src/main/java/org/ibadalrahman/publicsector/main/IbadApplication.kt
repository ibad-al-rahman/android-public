package org.ibadalrahman.publicsector.main

import android.app.Application
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

    override fun onCreate() {
        super.onCreate()
        System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

        notificationPoster.ensureChannels()
        notificationRearmObserver.start()

        // Fire-and-forget: refresh Remote Config in the background so startup never waits on the
        // network. The force-update flag starts false and flips if/when the fetch resolves true.
        CoroutineScope(SupervisorJob() + coroutineDispatchers.io).launch {
            remoteConfigRepository.refresh()
        }
    }
}
