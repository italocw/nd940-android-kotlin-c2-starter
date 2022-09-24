package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidsDatabase
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NetworkAsteroidContainer
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class Repository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getAsteroids()) {
            it.asDomainModel()
        }

    val todaysAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getTodayAsteroids()) {
            it.asDomainModel()
        }
    val nextSevenDaysAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getNextSevenDaysTodayAsteroids()) {
            it.asDomainModel()
        }

    val pictureOfDay = MutableLiveData<PictureOfDay>()

    suspend fun fetchPictureOfDay() {
        try {
            val returnedPictureOfDay = NasaApi.retrofitService.getPictureOfDay()
            if (returnedPictureOfDay.mediaType == "image") {
                pictureOfDay.value = returnedPictureOfDay
            }

        } catch (e: Exception) {
            Timber.log(Log.ERROR, e.message)
        }
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
            } catch (e: Exception) {
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