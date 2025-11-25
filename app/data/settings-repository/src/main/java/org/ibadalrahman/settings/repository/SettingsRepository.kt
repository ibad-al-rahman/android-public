package org.ibadalrahman.settings.repository

import kotlinx.coroutines.flow.StateFlow
import org.ibadalrahman.settings.repository.data.domain.Theme

interface SettingsRepository {
    val themeFlow: StateFlow<Theme>
    fun saveTheme(theme: Theme)
    fun getTheme(): Theme?
}
