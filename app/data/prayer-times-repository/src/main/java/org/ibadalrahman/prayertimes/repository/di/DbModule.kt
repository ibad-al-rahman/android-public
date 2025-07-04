package org.ibadalrahman.prayertimes.repository.di

import android.content.Context
import org.ibadalrahman.prayertimes.repository.data.local.PrayerTimesDao
import org.ibadalrahman.prayertimes.repository.data.local.PrayerTimesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {
    @Provides
    @Singleton
    fun providesPrayerTimesDao(
        @ApplicationContext context: Context,
    ): PrayerTimesDao = PrayerTimesDatabase.getInstance(context).prayerTimesDao()
}
