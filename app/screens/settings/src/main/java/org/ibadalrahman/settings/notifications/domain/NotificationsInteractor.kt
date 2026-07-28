package org.ibadalrahman.settings.notifications.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.ibadalrahman.mvi.BaseInteractor
import org.ibadalrahman.settings.notifications.domain.entity.NotificationsAction
import org.ibadalrahman.settings.notifications.domain.entity.NotificationsResult
import javax.inject.Inject

/**
 * No-op interactor. This screen is a UI shell: intentions map directly to state results in the
 * ViewModel router, so no domain action is ever dispatched here.
 *
 * TODO: persist the toggle/time state (e.g. via SettingsRepository) and request the
 *  POST_NOTIFICATIONS permission when notifications are enabled.
 */
class NotificationsInteractor @Inject constructor() :
    BaseInteractor<NotificationsAction, NotificationsResult> {
    override suspend fun resultFrom(action: NotificationsAction): Flow<NotificationsResult> =
        emptyFlow()
}
