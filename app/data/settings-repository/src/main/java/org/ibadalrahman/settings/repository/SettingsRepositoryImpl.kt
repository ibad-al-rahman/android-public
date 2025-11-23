package org.ibadalrahman.settings.repository

import org.ibadalrahman.settings.repository.data.domain.Theme
import org.ibadalrahman.settings.repository.data.local.SettingsLocalDataSource
import javax.inject.Inject


class SettingsRepositoryImpl @Inject constructor(
    private val localDatasource: SettingsLocalDataSource
): SettingsRepository {

    override fun saveTheme(theme: Theme) {
        localDatasource.saveTheme(theme)
    }

    override fun getTheme(): Theme? = localDatasource.getTheme()

}
