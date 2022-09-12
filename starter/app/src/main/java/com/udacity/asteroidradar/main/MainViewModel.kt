package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    private val _imageOfTheDayResponse = MutableLiveData<PictureOfDay>()
    val imageOfTheDayResponse: LiveData<PictureOfDay>
        get() = _imageOfTheDayResponse

    private val _neosResponse = MutableLiveData<List<Asteroid>>()
    val neosResponse: LiveData<List<Asteroid>>
        get() = _neosResponse

    init {
        getImageOfTheDay()
        getNeos()
    }
    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
    }

    var asteroids = MutableLiveData<List<Asteroid>>()

    init {
        asteroids.value = listOf()
    }

    private fun getImageOfTheDay() {
        NasaApi.retrofitService.getImageOfTheDay().enqueue(object : Callback<PictureOfDay> {
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                _imageOfTheDayResponse.value = response.body()
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                //_imageOfTheDayResponse.value = "Failure" + t.message
            }

        })
    }

    private fun getNeos() {
        NasaApi.retrofitService.getFeedWithNeosList().enqueue(object : Callback<JSONObject> {
            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                _neosResponse.value = parseAsteroidsJsonResult(response.body()!!)
            }

            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
             //   _neosResponse.value = "Failure" + t.message
            }

        })
    }

}