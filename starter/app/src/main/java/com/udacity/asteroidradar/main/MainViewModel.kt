package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidsDatabase
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.isInternetAvailable
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    private val database = AsteroidsDatabase.getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    val asteroids = asteroidsRepository.asteroids
    val todaysAsteroids = asteroidsRepository.todaysAsteroids
    val nextSevenDaysAsteroids = asteroidsRepository.nextSevenDaysAsteroids


    //  val selectedAsteroids = MutableLiveData<List<Asteroid>>()


    init {
        if (isInternetAvailable()) {
            getPictureOfDay()
            if (asteroids.value.isNullOrEmpty()) {
                refreshAsteroidData()
            }
        }
    }

    private fun refreshAsteroidData() {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
        }
    }


    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
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
}
