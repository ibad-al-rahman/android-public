package org.ibadalrahman.network.di

import com.google.gson.GsonBuilder
import org.ibadalrahman.network.interceptor.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    @Named("okHttpClient")
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(chuckerInterceptor.interceptor())
            .build()

    @Singleton
    @Provides
    fun providesGsonConverterFactory() = GsonConverterFactory.create(
        GsonBuilder().serializeNulls().create()
    )

    @Provides
    @Named("baseUrl")
    fun providesBaseUrl() = "https://ibad-al-rahman.github.io/prayer-times"

    @Singleton
    @Provides
    @Named("retrofit")
    fun providesRetrofit(
        @Named("baseUrl") baseUrl: String,
        @Named("okHttpClient") okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl("$baseUrl/")
        .client(okHttpClient)
        .build()
}
