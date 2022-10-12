package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidsDatabase
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.isInternetAvailable
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch


class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val database = AsteroidsDatabase.getInstance(application)
    private val repository = Repository(database)

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    private val _pictureOfDay = repository.pictureOfDay
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    val asteroids = repository.asteroids
    val todaysAsteroids = repository.todaysAsteroids
    val nextSevenDaysAsteroids = repository.nextSevenDaysAsteroids

    init {
        fetchOnlineDataIfHasInternetConnection()
    }

    private fun fetchOnlineDataIfHasInternetConnection() {
        viewModelScope.launch {
            if (isInternetAvailable()) {
                repository.fetchPictureOfDay()
                if (todaysAsteroids.value.isNullOrEmpty()) {
                    repository.refreshAsteroids()
                }
            }
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
    }
}
