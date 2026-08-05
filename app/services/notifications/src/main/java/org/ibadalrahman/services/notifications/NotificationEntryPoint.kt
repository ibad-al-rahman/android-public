package org.ibadalrahman.services.notifications

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt access point for the notification receivers. BroadcastReceivers can't be `@AndroidEntryPoint`
 * cleanly, so — like the widget's `WidgetEntryPoint` — they reach the singleton graph via
 * `EntryPoints.get(context.applicationContext, NotificationEntryPoint::class.java)`.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotificationEntryPoint {
    fun scheduler(): NotificationScheduler
    fun poster(): NotificationPoster
}
