package com.ibadalrahman.prayertimes.repository.di

import com.ibadalrahman.prayertimes.repository.PrayerTimesRepository
import com.ibadalrahman.prayertimes.repository.PrayerTimesRepositoryImpl
import com.ibadalrahman.prayertimes.repository.data.local.PrayerTimesLocalDataSource
import com.ibadalrahman.prayertimes.repository.data.local.PrayerTimesLocalDataSourceImpl
import com.ibadalrahman.prayertimes.repository.data.remote.PrayerTimesRemoteDataSource
import com.ibadalrahman.prayertimes.repository.data.remote.PrayerTimesRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PrayerTimesRepositoryBinds {
    @Binds
    abstract fun bindsPrayerTimesRemoteDataSource(
        dataSource: PrayerTimesRemoteDataSourceImpl
    ): PrayerTimesRemoteDataSource

    @Binds
    abstract fun bindsPrayerTimesLocalDataSource(
        dataSource: PrayerTimesLocalDataSourceImpl
    ): PrayerTimesLocalDataSource

    @Binds
    abstract fun bindsPrayerTimesRepository(
        repository: PrayerTimesRepositoryImpl
    ): PrayerTimesRepository
}
