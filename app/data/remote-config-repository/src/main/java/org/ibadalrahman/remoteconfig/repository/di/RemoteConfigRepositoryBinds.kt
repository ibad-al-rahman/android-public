package org.ibadalrahman.remoteconfig.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ibadalrahman.remoteconfig.repository.RemoteConfigRepository
import org.ibadalrahman.remoteconfig.repository.RemoteConfigRepositoryImpl
import org.ibadalrahman.remoteconfig.repository.data.remote.RemoteConfigDataSource
import org.ibadalrahman.remoteconfig.repository.data.remote.RemoteConfigDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigRepositoryBinds {
    @Binds
    abstract fun bindsRemoteConfigDataSource(
        dataSource: RemoteConfigDataSourceImpl
    ): RemoteConfigDataSource

    @Binds
    abstract fun bindsRemoteConfigRepository(
        repository: RemoteConfigRepositoryImpl
    ): RemoteConfigRepository
}
