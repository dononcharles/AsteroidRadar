package com.udacity.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data.local.dao.AsteroidDao
import com.udacity.asteroidradar.data.local.dao.PictureOfDayDao
import com.udacity.asteroidradar.data.local.models.Asteroid
import com.udacity.asteroidradar.data.local.models.PictureOfDay

/**
 * @author Komi Donon
 * @since 5/12/2023
 */

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao

    companion object {
        private lateinit var INSTANCE: AppDatabase
        private const val DATABASE_NAME = "asteroids"

        fun getDatabase(context: Context): AppDatabase {
            synchronized(AppDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = androidx.room.Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME,
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
