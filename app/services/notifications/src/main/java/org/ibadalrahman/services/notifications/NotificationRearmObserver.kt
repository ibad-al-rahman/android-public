package org.ibadalrahman.services.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.ibadalrahman.settings.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Re-arms the rolling alarm whenever the user changes their notification preferences. Started once
 * from the Application; collects [SettingsRepository.notificationSettingsFlow] for the process
 * lifetime. The initial value is dropped because the app also re-arms on start via [start].
 */
@Singleton
class NotificationRearmObserver @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val scheduler: NotificationScheduler,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun start() {
        // Re-arm once on start (covers app-process recreation), then on every subsequent change.
        scheduler.reschedule()
        settingsRepository.notificationSettingsFlow
            .drop(1)
            .onEach { scheduler.reschedule() }
            .launchIn(scope)
    }
}
