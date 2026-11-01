package org.ibadalrahman.tracker

import org.ibadalrahman.tracker.entity.EventData
import org.ibadalrahman.tracker.providers.AnalyticsProvider
import org.ibadalrahman.tracker.providers.CrashlyticsProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single entry point for telemetry. Features inject [Tracker] — not the Firebase providers — and
 * call [trackEvent] / [recordException]. Mirrors the Proximie Android `TrackerManager` facade,
 * scaled down to this app (Analytics + Crashlytics only).
 */
@Singleton
class Tracker @Inject constructor(
    private val analyticsProvider: AnalyticsProvider,
    private val crashlyticsProvider: CrashlyticsProvider,
) {
    fun trackEvent(event: EventData) {
        analyticsProvider.log(event.toTrackingData())
    }

    /** Logs a non-fatal exception to Crashlytics. */
    fun recordException(throwable: Throwable) {
        crashlyticsProvider.recordException(throwable)
    }
}
