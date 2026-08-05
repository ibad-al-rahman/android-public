package org.ibadalrahman.publicsector.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import org.ibadalrahman.services.notifications.NotificationPoster
import org.ibadalrahman.services.notifications.NotificationRearmObserver
import javax.inject.Inject

@HiltAndroidApp
class IbadApplication: Application() {

    @Inject lateinit var notificationPoster: NotificationPoster
    @Inject lateinit var notificationRearmObserver: NotificationRearmObserver

    override fun onCreate() {
        super.onCreate()
        System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

        notificationPoster.ensureChannels()
        notificationRearmObserver.start()
    }
}
