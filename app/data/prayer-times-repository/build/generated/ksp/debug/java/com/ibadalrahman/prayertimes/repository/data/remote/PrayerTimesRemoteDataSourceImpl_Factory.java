package com.ibadalrahman.prayertimes.repository.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class PrayerTimesRemoteDataSourceImpl_Factory implements Factory<PrayerTimesRemoteDataSourceImpl> {
  private final Provider<PrayerTimesApi> prayerTimesApiProvider;

  public PrayerTimesRemoteDataSourceImpl_Factory(Provider<PrayerTimesApi> prayerTimesApiProvider) {
    this.prayerTimesApiProvider = prayerTimesApiProvider;
  }

  @Override
  public PrayerTimesRemoteDataSourceImpl get() {
    return newInstance(prayerTimesApiProvider.get());
  }

  public static PrayerTimesRemoteDataSourceImpl_Factory create(
      Provider<PrayerTimesApi> prayerTimesApiProvider) {
    return new PrayerTimesRemoteDataSourceImpl_Factory(prayerTimesApiProvider);
  }

  public static PrayerTimesRemoteDataSourceImpl newInstance(PrayerTimesApi prayerTimesApi) {
    return new PrayerTimesRemoteDataSourceImpl(prayerTimesApi);
  }
}
