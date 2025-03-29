package com.ibadalrahman.prayertimes.repository;

import com.ibadalrahman.prayertimes.repository.data.local.PrayerTimesLocalDataSource;
import com.ibadalrahman.prayertimes.repository.data.remote.PrayerTimesRemoteDataSource;
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
public final class PrayerTimesRepositoryImpl_Factory implements Factory<PrayerTimesRepositoryImpl> {
  private final Provider<PrayerTimesRemoteDataSource> remoteDataSourceProvider;

  private final Provider<PrayerTimesLocalDataSource> localDatasourceProvider;

  public PrayerTimesRepositoryImpl_Factory(
      Provider<PrayerTimesRemoteDataSource> remoteDataSourceProvider,
      Provider<PrayerTimesLocalDataSource> localDatasourceProvider) {
    this.remoteDataSourceProvider = remoteDataSourceProvider;
    this.localDatasourceProvider = localDatasourceProvider;
  }

  @Override
  public PrayerTimesRepositoryImpl get() {
    return newInstance(remoteDataSourceProvider.get(), localDatasourceProvider.get());
  }

  public static PrayerTimesRepositoryImpl_Factory create(
      Provider<PrayerTimesRemoteDataSource> remoteDataSourceProvider,
      Provider<PrayerTimesLocalDataSource> localDatasourceProvider) {
    return new PrayerTimesRepositoryImpl_Factory(remoteDataSourceProvider, localDatasourceProvider);
  }

  public static PrayerTimesRepositoryImpl newInstance(PrayerTimesRemoteDataSource remoteDataSource,
      PrayerTimesLocalDataSource localDatasource) {
    return new PrayerTimesRepositoryImpl(remoteDataSource, localDatasource);
  }
}
