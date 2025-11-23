package org.ibadalrahman.settings.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ibadalrahman.settings.repository.SettingsRepository
import org.ibadalrahman.settings.repository.SettingsRepositoryImpl
import org.ibadalrahman.settings.repository.data.local.SettingsLocalDataSource
import org.ibadalrahman.settings.repository.data.local.SettingsLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsRepositoryBinds {
    @Binds
    abstract fun bindsSettingsLocalDataSource(
        dataSource: SettingsLocalDataSourceImpl
    ): SettingsLocalDataSource

    @Binds
    abstract fun bindsSettingsRepository(
        repository: SettingsRepositoryImpl
    ): SettingsRepository
}
