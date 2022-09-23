package com.udacity.asteroidradar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)

abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDatabaseDao: AsteroidDao

    companion object {
        @Volatile
        private lateinit var INSTANCE:AsteroidsDatabase

        fun getInstance(context: Context): AsteroidsDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidsDatabase::class.java,
                        "asteroid_history_database"
                    ).fallbackToDestructiveMigration().build()
                }

                return INSTANCE
            }
        }
    }
}