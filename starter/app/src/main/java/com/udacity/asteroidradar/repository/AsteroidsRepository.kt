package com.udacity.asteroidradar.repository

import android.util.Log
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
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getTodayAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        val formattedCurrentDateTime = getFormattedCurrentDateTime()
        withContext(Dispatchers.IO) {
            try {
                val networkAsteroid = parseAsteroidsJsonResult(
                    JSONObject(NasaApi.retrofitService.getFeedWithNeos(formattedCurrentDateTime))
                )

                val networkAsteroidContainer = NetworkAsteroidContainer(networkAsteroid)
                val asteroidDatabaseDao = database.asteroidDatabaseDao

                asteroidDatabaseDao.insertAll(*networkAsteroidContainer.asDatabaseModel())
                asteroidDatabaseDao.deleteYesterdayAsteroids()
            }catch (e: Exception) {
                Timber.log(Log.ERROR, e.message)
            }
        }
    }

    private fun getFormattedCurrentDateTime(): String {
        val isDatePattern = "yyyy-MM-dd"
        val format = SimpleDateFormat(isDatePattern)

        return format.format(Calendar.getInstance().time)
    }

}