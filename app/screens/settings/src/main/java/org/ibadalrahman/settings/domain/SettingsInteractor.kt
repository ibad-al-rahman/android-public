package org.ibadalrahman.settings.domain

import org.ibadalrahman.mvi.BaseInteractor
import org.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import org.ibadalrahman.settings.domain.entity.SettingsAction
import org.ibadalrahman.settings.domain.entity.SettingsResult
import kotlinx.coroutines.flow.flowOf

class SettingsInteractor @Inject constructor(
    private val prayerTimesRepository: PrayerTimesRepository
): BaseInteractor<SettingsAction, SettingsResult> {
    override suspend fun resultFrom(action: SettingsAction): Flow<SettingsResult> =
        when (action) {
            SettingsAction.ContactUs -> flowOf(SettingsResult.ContactUs)
            SettingsAction.Donate -> flowOf(SettingsResult.Donate)
            SettingsAction.ClearCache -> {
                prayerTimesRepository.clear()
                flowOf(SettingsResult.NoOp)
            }
            is SettingsAction.ChangeLanguage ->
                flowOf(SettingsResult.LanguageChanged(action.language))
            is SettingsAction.ChangeTheme ->
                flowOf(SettingsResult.ThemeChanged(action.theme))
        }
}
