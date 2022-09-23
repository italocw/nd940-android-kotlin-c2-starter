package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.sql.Time


private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
  //  .addConverterFactory(GsonConverterFactory.create())

    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL).build()


interface NasaApiService {
    @GET("neo/rest/v1/feed?&api_key=$API_KEY")
    suspend fun getFeedWithNeos(@Query("start_date") startDate:String):
            String


   // suspend fun getFeedWithNeos(@Path("current-date") currentDate: String): String

    @GET("planetary/apod?api_key=$API_KEY")
    suspend fun getPictureOfDay():
            PictureOfDay
}


object NasaApi {
    val retrofitService: NasaApiService by lazy { retrofit.create(NasaApiService::class.java) }

}