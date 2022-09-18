package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
   @Query("SELECT * FROM  asteroid_table where close_approach_date = date('now','localtime','start of day') ORDER BY close_approach_date DESC ")
   fun  getTodayAsteroids(): LiveData<List<Asteroid>>

    @Insert
    suspend fun insert(asteroid: Asteroid)

 //   @Query("DELETE * FROM  asteroid_table where close_approach_date = date('now','localtime','start of day', '-1 day') ")
   // suspend fun  deleteYesterdayAsteroids()
}