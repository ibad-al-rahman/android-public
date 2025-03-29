package com.ibadalrahman.prayertimes.repository.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity;
import com.ibadalrahman.prayertimes.repository.data.local.entities.EventEntity;
import com.ibadalrahman.prayertimes.repository.data.local.entities.PrayerTimesEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PrayerTimesDao_Impl implements PrayerTimesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DayPrayerTimesEntity> __insertionAdapterOfDayPrayerTimesEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public PrayerTimesDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDayPrayerTimesEntity = new EntityInsertionAdapter<DayPrayerTimesEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `day_prayer_times` (`id`,`gregorian`,`hijri`,`weekId`,`fajr`,`sunrise`,`dhuhr`,`asr`,`maghrib`,`ishaa`,`ar`,`en`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DayPrayerTimesEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getGregorian());
        statement.bindString(3, entity.getHijri());
        statement.bindString(4, entity.getWeekId());
        final PrayerTimesEntity _tmpPrayerTimes = entity.getPrayerTimes();
        statement.bindString(5, _tmpPrayerTimes.getFajr());
        statement.bindString(6, _tmpPrayerTimes.getSunrise());
        statement.bindString(7, _tmpPrayerTimes.getDhuhr());
        statement.bindString(8, _tmpPrayerTimes.getAsr());
        statement.bindString(9, _tmpPrayerTimes.getMaghrib());
        statement.bindString(10, _tmpPrayerTimes.getIshaa());
        final EventEntity _tmpEvent = entity.getEvent();
        if (_tmpEvent != null) {
          statement.bindString(11, _tmpEvent.getAr());
          if (_tmpEvent.getEn() == null) {
            statement.bindNull(12);
          } else {
            statement.bindString(12, _tmpEvent.getEn());
          }
        } else {
          statement.bindNull(11);
          statement.bindNull(12);
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM day_prayer_times";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final DayPrayerTimesEntity... prayerTimes) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDayPrayerTimesEntity.insert(prayerTimes);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public DayPrayerTimesEntity findById(final int id) {
    final String _sql = "SELECT * FROM day_prayer_times WHERE id = (?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGregorian = CursorUtil.getColumnIndexOrThrow(_cursor, "gregorian");
      final int _cursorIndexOfHijri = CursorUtil.getColumnIndexOrThrow(_cursor, "hijri");
      final int _cursorIndexOfWeekId = CursorUtil.getColumnIndexOrThrow(_cursor, "weekId");
      final int _cursorIndexOfFajr = CursorUtil.getColumnIndexOrThrow(_cursor, "fajr");
      final int _cursorIndexOfSunrise = CursorUtil.getColumnIndexOrThrow(_cursor, "sunrise");
      final int _cursorIndexOfDhuhr = CursorUtil.getColumnIndexOrThrow(_cursor, "dhuhr");
      final int _cursorIndexOfAsr = CursorUtil.getColumnIndexOrThrow(_cursor, "asr");
      final int _cursorIndexOfMaghrib = CursorUtil.getColumnIndexOrThrow(_cursor, "maghrib");
      final int _cursorIndexOfIshaa = CursorUtil.getColumnIndexOrThrow(_cursor, "ishaa");
      final int _cursorIndexOfAr = CursorUtil.getColumnIndexOrThrow(_cursor, "ar");
      final int _cursorIndexOfEn = CursorUtil.getColumnIndexOrThrow(_cursor, "en");
      final DayPrayerTimesEntity _result;
      if (_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpGregorian;
        _tmpGregorian = _cursor.getString(_cursorIndexOfGregorian);
        final String _tmpHijri;
        _tmpHijri = _cursor.getString(_cursorIndexOfHijri);
        final String _tmpWeekId;
        _tmpWeekId = _cursor.getString(_cursorIndexOfWeekId);
        final PrayerTimesEntity _tmpPrayerTimes;
        final String _tmpFajr;
        _tmpFajr = _cursor.getString(_cursorIndexOfFajr);
        final String _tmpSunrise;
        _tmpSunrise = _cursor.getString(_cursorIndexOfSunrise);
        final String _tmpDhuhr;
        _tmpDhuhr = _cursor.getString(_cursorIndexOfDhuhr);
        final String _tmpAsr;
        _tmpAsr = _cursor.getString(_cursorIndexOfAsr);
        final String _tmpMaghrib;
        _tmpMaghrib = _cursor.getString(_cursorIndexOfMaghrib);
        final String _tmpIshaa;
        _tmpIshaa = _cursor.getString(_cursorIndexOfIshaa);
        _tmpPrayerTimes = new PrayerTimesEntity(_tmpFajr,_tmpSunrise,_tmpDhuhr,_tmpAsr,_tmpMaghrib,_tmpIshaa);
        final EventEntity _tmpEvent;
        if (!(_cursor.isNull(_cursorIndexOfAr) && _cursor.isNull(_cursorIndexOfEn))) {
          final String _tmpAr;
          _tmpAr = _cursor.getString(_cursorIndexOfAr);
          final String _tmpEn;
          if (_cursor.isNull(_cursorIndexOfEn)) {
            _tmpEn = null;
          } else {
            _tmpEn = _cursor.getString(_cursorIndexOfEn);
          }
          _tmpEvent = new EventEntity(_tmpAr,_tmpEn);
        } else {
          _tmpEvent = null;
        }
        _result = new DayPrayerTimesEntity(_tmpId,_tmpGregorian,_tmpHijri,_tmpPrayerTimes,_tmpWeekId,_tmpEvent);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
