package org.ibadalrahman.tracker.providers

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import javax.inject.Inject
import javax.inject.Singleton

/** Thin wrapper over Firebase Crashlytics for logging non-fatal exceptions and custom keys. */
@Singleton
class CrashlyticsProvider @Inject constructor() {

    private val crashlytics: FirebaseCrashlytics = Firebase.crashlytics

    /** Records a non-fatal exception. Fatal crashes are captured automatically by the SDK. */
    fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    fun log(message: String) {
        crashlytics.log(message)
    }

    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}
