package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidDatabaseDao
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber


class MainViewModel(    val database: AsteroidDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _asteroids = database.getTodayAsteroids()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
    }


    init {

        getOnlineData()
    }


    private fun getOnlineData() {
        getPictureOfDay()
        getNeos()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                val returnedPictureOfDay = NasaApi.retrofitService.getPictureOfDay()
                if (returnedPictureOfDay.mediaType == "image") {
                    _pictureOfDay.value = returnedPictureOfDay
                }

            } catch (e: Exception) {
                Timber.log(Log.ERROR, e.message)
            }
        }
    }

    private fun getNeos() {
        viewModelScope.launch {
            try {
                val responseString = NasaApi.retrofitService.getFeedWithNeos()
               val asteroids = parseAsteroidsJsonResult(JSONObject(responseString))

                insertNeosOnDatabase(asteroids)
            } catch (e: Exception) {
                Timber.log(Log.ERROR, e.message)
            }
        }

    }

    private suspend fun insertNeosOnDatabase(asteroids: ArrayList<Asteroid>) {
       asteroids.forEach {  database.insert(it) }
    }

}