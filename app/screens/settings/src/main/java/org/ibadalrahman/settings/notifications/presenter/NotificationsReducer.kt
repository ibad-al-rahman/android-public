package org.ibadalrahman.settings.notifications.presenter

import org.ibadalrahman.settings.notifications.domain.entity.NotificationsResult
import org.ibadalrahman.settings.notifications.presenter.entity.NotificationsScreenState

object NotificationsReducer {
    fun reduce(
        prevState: NotificationsScreenState,
        result: NotificationsResult
    ): NotificationsScreenState = when (result) {
        is NotificationsResult.Loaded -> NotificationsScreenState.fromSettings(result.settings)
    }
}
