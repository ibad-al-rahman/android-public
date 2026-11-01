package org.ibadalrahman.forceupdate.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * [isStartingUpdate] flips once the user taps update, so the screen can disable the button while the
 * Play update flow is being launched.
 */
@Stable
@Immutable
data class ForceUpdateScreenState(
    val isStartingUpdate: Boolean,
) {
    companion object {
        val Empty = ForceUpdateScreenState(isStartingUpdate = false)
    }
}
