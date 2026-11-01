package org.ibadalrahman.remoteconfig.repository.data.domain

/**
 * Firebase Remote Config keys. Mirrors the iOS app's `FeatureFlagKey` so the same Firebase
 * parameters drive both platforms. [key] is the exact parameter name configured in the Firebase
 * console.
 */
enum class FeatureFlagKey(val key: String) {
    ForceUpdate("ff_mobile_forceUpdate"),
}
