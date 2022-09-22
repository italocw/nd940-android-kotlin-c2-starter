package com.udacity.asteroidradar

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.database.DatabaseAsteroid

@Dao
interface AsteroidDao {
    @Query("select * from  asteroid_table where close_approach_date = date('now','localtime','start of day') order by close_approach_date desc ")
    fun getTodayAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from  asteroid_table")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM asteroid_table where close_approach_date <= date('now','localtime','start of day', '-1 day') ")
    suspend fun deleteYesterdayAsteroids()
}