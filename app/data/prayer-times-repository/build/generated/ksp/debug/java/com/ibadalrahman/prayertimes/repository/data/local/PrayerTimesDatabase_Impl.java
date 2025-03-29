package com.ibadalrahman.prayertimes.repository.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PrayerTimesDatabase_Impl extends PrayerTimesDatabase {
  private volatile PrayerTimesDao _prayerTimesDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `day_prayer_times` (`id` INTEGER NOT NULL, `gregorian` TEXT NOT NULL, `hijri` TEXT NOT NULL, `weekId` TEXT NOT NULL, `fajr` TEXT NOT NULL, `sunrise` TEXT NOT NULL, `dhuhr` TEXT NOT NULL, `asr` TEXT NOT NULL, `maghrib` TEXT NOT NULL, `ishaa` TEXT NOT NULL, `ar` TEXT, `en` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f7cbb1c21d5a052fc538487959fc4ea5')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `day_prayer_times`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDayPrayerTimes = new HashMap<String, TableInfo.Column>(12);
        _columnsDayPrayerTimes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("gregorian", new TableInfo.Column("gregorian", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("hijri", new TableInfo.Column("hijri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("weekId", new TableInfo.Column("weekId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("fajr", new TableInfo.Column("fajr", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("sunrise", new TableInfo.Column("sunrise", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("dhuhr", new TableInfo.Column("dhuhr", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("asr", new TableInfo.Column("asr", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("maghrib", new TableInfo.Column("maghrib", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("ishaa", new TableInfo.Column("ishaa", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("ar", new TableInfo.Column("ar", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPrayerTimes.put("en", new TableInfo.Column("en", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDayPrayerTimes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDayPrayerTimes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDayPrayerTimes = new TableInfo("day_prayer_times", _columnsDayPrayerTimes, _foreignKeysDayPrayerTimes, _indicesDayPrayerTimes);
        final TableInfo _existingDayPrayerTimes = TableInfo.read(db, "day_prayer_times");
        if (!_infoDayPrayerTimes.equals(_existingDayPrayerTimes)) {
          return new RoomOpenHelper.ValidationResult(false, "day_prayer_times(com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity).\n"
                  + " Expected:\n" + _infoDayPrayerTimes + "\n"
                  + " Found:\n" + _existingDayPrayerTimes);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f7cbb1c21d5a052fc538487959fc4ea5", "c1007371b600246c1b77297403015369");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "day_prayer_times");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `day_prayer_times`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PrayerTimesDao.class, PrayerTimesDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PrayerTimesDao prayerTimesDao() {
    if (_prayerTimesDao != null) {
      return _prayerTimesDao;
    } else {
      synchronized(this) {
        if(_prayerTimesDao == null) {
          _prayerTimesDao = new PrayerTimesDao_Impl(this);
        }
        return _prayerTimesDao;
      }
    }
  }
}
