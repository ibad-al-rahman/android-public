package org.ibadalrahman.settings.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ibadalrahman.mvi.BaseInteractor
import org.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import org.ibadalrahman.settings.domain.entity.SettingsAction
import org.ibadalrahman.settings.domain.entity.SettingsResult
import kotlinx.coroutines.flow.flowOf
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.repository.SettingsRepository
import org.ibadalrahman.settings.repository.data.domain.Theme

class SettingsInteractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prayerTimesRepository: PrayerTimesRepository,
    private val settingsRepository: SettingsRepository
): BaseInteractor<SettingsAction, SettingsResult> {
    override suspend fun resultFrom(action: SettingsAction): Flow<SettingsResult> =
        when (action) {
            SettingsAction.ContactUs -> flowOf(SettingsResult.ContactUs)
            SettingsAction.Donate -> flowOf(SettingsResult.Donate)
            SettingsAction.ShareApp -> {
                val shareText = listOf(
                    context.getString(R.string.share_app_msg_header),
                    "\n",
                    context.getString(R.string.android),
                    " ",
                    "https://play.google.com/store/apps/details?id=org.ibadalrahman.publicsector",
                    "\n",
                    context.getString(R.string.ios),
                    " ",
                    "https://apps.apple.com/lb/app/ibad-al-rahman/id6739705601"
                ).joinToString("")
                flowOf(SettingsResult.ShareApp(shareText))
            }
            SettingsAction.ClearCache -> {
                prayerTimesRepository.clear()
                flowOf(SettingsResult.NoOp)
            }
            is SettingsAction.ChangeLanguage ->
                flowOf(SettingsResult.LanguageChanged(action.language))
            is SettingsAction.ChangeTheme -> changeTheme(action.theme)
        }

    private fun changeTheme(theme: Theme): Flow<SettingsResult> = flow {
        settingsRepository.saveTheme(theme)
        emit(SettingsResult.ThemeChanged(theme))
    }
}
