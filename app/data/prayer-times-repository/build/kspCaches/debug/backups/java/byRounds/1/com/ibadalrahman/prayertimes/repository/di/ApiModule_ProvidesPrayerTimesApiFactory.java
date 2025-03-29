package com.ibadalrahman.prayertimes.repository.di;

import com.ibadalrahman.prayertimes.repository.data.remote.PrayerTimesApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class ApiModule_ProvidesPrayerTimesApiFactory implements Factory<PrayerTimesApi> {
  private final ApiModule module;

  private final Provider<Retrofit> retrofitProvider;

  public ApiModule_ProvidesPrayerTimesApiFactory(ApiModule module,
      Provider<Retrofit> retrofitProvider) {
    this.module = module;
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public PrayerTimesApi get() {
    return providesPrayerTimesApi(module, retrofitProvider.get());
  }

  public static ApiModule_ProvidesPrayerTimesApiFactory create(ApiModule module,
      Provider<Retrofit> retrofitProvider) {
    return new ApiModule_ProvidesPrayerTimesApiFactory(module, retrofitProvider);
  }

  public static PrayerTimesApi providesPrayerTimesApi(ApiModule instance, Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(instance.providesPrayerTimesApi(retrofit));
  }
}
