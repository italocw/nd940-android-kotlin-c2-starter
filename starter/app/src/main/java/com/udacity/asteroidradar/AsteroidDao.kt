package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.DatabaseAsteroid

@Dao
interface AsteroidDao {
    @Query("select * from  asteroid_table where close_approach_date = date('now','localtime','start of day') order by close_approach_date desc ")
    fun getTodayAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from  asteroid_table order by close_approach_date desc ")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from  asteroid_table where close_approach_date >= date('now','localtime','start of day') AND   close_approach_date<=  date('now','localtime','start of day', '7 day')order by close_approach_date desc ")
    fun getNextSevenDaysTodayAsteroids(): LiveData<List<DatabaseAsteroid>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroid: DatabaseAsteroid)

    @Query("DELETE FROM asteroid_table where close_approach_date <= date('now','localtime','start of day', '-1 day') ")
    suspend fun deleteYesterdayAsteroids()
}