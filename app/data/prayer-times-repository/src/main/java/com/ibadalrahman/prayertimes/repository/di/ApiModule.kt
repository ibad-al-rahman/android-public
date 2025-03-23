package com.ibadalrahman.prayertimes.repository.di

import com.ibadalrahman.prayertimes.repository.data.remote.PrayerTimesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun providesPrayerTimesApi(
        @Named("retrofit") retrofit: Retrofit
    ): PrayerTimesApi =
        retrofit.create(PrayerTimesApi::class.java)
}
