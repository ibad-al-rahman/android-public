package com.ibadalrahman.settings.domain

import com.ibadalrahman.mvi.BaseInteractor
import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.ibadalrahman.settings.domain.entity.SettingsAction
import com.ibadalrahman.settings.domain.entity.SettingsResult
import kotlinx.coroutines.flow.flowOf

class SettingsInteractor @Inject constructor(
    private val prayerTimesRepository: PrayerTimesRepository
): BaseInteractor<SettingsAction, SettingsResult> {
    override suspend fun resultFrom(action: SettingsAction): Flow<SettingsResult> =
        when (action) {
            is SettingsAction.ChangeLanguage ->
                flowOf(SettingsResult.LanguageChanged(action.language))
        }
}
