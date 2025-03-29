package com.ibadalrahman.prayertimes.repository.data.local;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class PrayerTimesLocalDataSourceImpl_Factory implements Factory<PrayerTimesLocalDataSourceImpl> {
  private final Provider<PrayerTimesDao> prayerTimesDaoProvider;

  public PrayerTimesLocalDataSourceImpl_Factory(Provider<PrayerTimesDao> prayerTimesDaoProvider) {
    this.prayerTimesDaoProvider = prayerTimesDaoProvider;
  }

  @Override
  public PrayerTimesLocalDataSourceImpl get() {
    return newInstance(prayerTimesDaoProvider.get());
  }

  public static PrayerTimesLocalDataSourceImpl_Factory create(
      Provider<PrayerTimesDao> prayerTimesDaoProvider) {
    return new PrayerTimesLocalDataSourceImpl_Factory(prayerTimesDaoProvider);
  }

  public static PrayerTimesLocalDataSourceImpl newInstance(PrayerTimesDao prayerTimesDao) {
    return new PrayerTimesLocalDataSourceImpl(prayerTimesDao);
  }
}
