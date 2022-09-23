package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidsDatabase
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit
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

    init {
        getPictureOfDay()
        if (asteroids.value!!.isEmpty()) {
            refreshAsteroidData()
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

    fun onMenuItemSelected(itemId: Int) {
        when (itemId) {
            R.id.show_all_menu -> {
asteroidsRepository.todayAsteroids.
            }
            R.id.show_today_asteroids_menu -> {}

            R.id.show_saved_asteroids_menu -> {}
        }
    }
}