package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _asteroids =  MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


    init {
  //      getPictureOfDay()
      //  getNeos()
    }
    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
    }


    init {
        getPictureOfDay()
        _asteroids.value = listOf()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try{
                _pictureOfDay.value =    NasaApi.retrofitService.getPictureOfDay()

            }catch (e: Exception) {

            }
        }
    }

    private fun getNeos() {
       /* viewModelScope.launch {
            try {
                val feedWithNeosResponse = NasaApi.retrofitService.getFeedWithNeos()
                _asteroids.value = parseAsteroidsJsonResult(feedWithNeosResponse)
            } catch (e: Exception) {
            }
        }*/
    }
}