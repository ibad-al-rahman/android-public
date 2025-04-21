package com.ibadalrahman.prayertimes.repository.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ibadalrahman.prayertimes.repository.data.local.entities.DayPrayerTimesEntity
import com.ibadalrahman.prayertimes.repository.data.local.entities.WeekEntity

@Database(
    entities = [DayPrayerTimesEntity::class, WeekEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PrayerTimesDatabase: RoomDatabase() {
    abstract fun prayerTimesDao(): PrayerTimesDao

    companion object {
        private const val DATABASE_NAME = "com.ibadalrahman.prayertimes.database"
        private var instance: PrayerTimesDatabase? = null

        fun getInstance(context: Context): PrayerTimesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, PrayerTimesDatabase::class.java, DATABASE_NAME).build()
    }
}
