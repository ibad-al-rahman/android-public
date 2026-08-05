package org.ibadalrahman.settings.notifications.domain.entity

import org.ibadalrahman.settings.repository.data.domain.NotificationSettings

/**
 * Every action persists the change and reloads the full snapshot, so a single [Loaded] result
 * carrying the fresh [NotificationSettings] keeps state consistent with what was persisted.
 */
sealed interface NotificationsResult {
    data class Loaded(val settings: NotificationSettings) : NotificationsResult
}
