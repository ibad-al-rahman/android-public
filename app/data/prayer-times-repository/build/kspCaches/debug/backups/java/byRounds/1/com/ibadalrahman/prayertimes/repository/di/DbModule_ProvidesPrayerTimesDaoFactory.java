package com.ibadalrahman.prayertimes.repository.di;

import android.content.Context;
import com.ibadalrahman.prayertimes.repository.data.local.PrayerTimesDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DbModule_ProvidesPrayerTimesDaoFactory implements Factory<PrayerTimesDao> {
  private final DbModule module;

  private final Provider<Context> contextProvider;

  public DbModule_ProvidesPrayerTimesDaoFactory(DbModule module,
      Provider<Context> contextProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
  }

  @Override
  public PrayerTimesDao get() {
    return providesPrayerTimesDao(module, contextProvider.get());
  }

  public static DbModule_ProvidesPrayerTimesDaoFactory create(DbModule module,
      Provider<Context> contextProvider) {
    return new DbModule_ProvidesPrayerTimesDaoFactory(module, contextProvider);
  }

  public static PrayerTimesDao providesPrayerTimesDao(DbModule instance, Context context) {
    return Preconditions.checkNotNullFromProvides(instance.providesPrayerTimesDao(context));
  }
}
