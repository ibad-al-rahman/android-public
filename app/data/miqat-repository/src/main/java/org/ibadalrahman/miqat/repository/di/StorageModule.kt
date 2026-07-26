package org.ibadalrahman.miqat.repository.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/** Qualifier + name for the module's private [SharedPreferences] store. */
const val MIQAT_PREFS = "miqat_prefs"

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {
    @Provides
    @Singleton
    @Named(MIQAT_PREFS)
    fun providesMiqatSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(MIQAT_PREFS, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesGson(): Gson = Gson()
}
