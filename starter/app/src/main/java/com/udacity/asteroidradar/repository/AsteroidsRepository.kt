package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidsDatabase
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NetworkAsteroidContainer
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val todayAsteroids: LiveData<List<Asteroid>> =  Transformations.map(database.asteroidDatabaseDao.getTodayAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val networkAsteroid =
                parseAsteroidsJsonResult(JSONObject(NasaApi.retrofitService.getFeedWithNeos()))
            val networkAsteroidContainer = NetworkAsteroidContainer(networkAsteroid)
            val asteroidDatabaseDao = database.asteroidDatabaseDao

            asteroidDatabaseDao.insertAll(*networkAsteroidContainer.asDatabaseModel())
           asteroidDatabaseDao.deleteYesterdayAsteroids()
        }
    }

}