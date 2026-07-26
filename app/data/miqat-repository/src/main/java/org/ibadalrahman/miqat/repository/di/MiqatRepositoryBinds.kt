package org.ibadalrahman.miqat.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ibadalrahman.miqat.repository.MiqatRepository
import org.ibadalrahman.miqat.repository.MiqatRepositoryImpl
import org.ibadalrahman.miqat.repository.data.local.MiqatLocalDataSource
import org.ibadalrahman.miqat.repository.data.local.MiqatLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class MiqatRepositoryBinds {
    @Binds
    abstract fun bindsMiqatLocalDataSource(
        dataSource: MiqatLocalDataSourceImpl,
    ): MiqatLocalDataSource

    @Binds
    abstract fun bindsMiqatRepository(
        repository: MiqatRepositoryImpl,
    ): MiqatRepository
}
