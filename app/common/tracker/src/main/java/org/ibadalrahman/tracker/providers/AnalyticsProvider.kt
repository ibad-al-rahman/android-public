package org.ibadalrahman.tracker.providers

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import org.ibadalrahman.tracker.entity.EventTrackerData
import javax.inject.Inject
import javax.inject.Singleton

/** Thin wrapper over Firebase Analytics so the SDK stays behind the [org.ibadalrahman.tracker.Tracker] facade. */
@Singleton
class AnalyticsProvider @Inject constructor() {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun log(event: EventTrackerData) {
        firebaseAnalytics.logEvent(event.eventName) {
            event.params.forEach { (key, value) -> param(key, value.toString()) }
        }
    }
}
