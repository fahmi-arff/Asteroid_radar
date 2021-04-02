package com.udacity.asteroidradar.network

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.models.PictureOfDay
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// retrofit use moshi to convert Json data so can be accesses to app
private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


interface AsteroidApiService {

    // use return as responseBody for parse by networkUtil
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsAsync(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): ResponseBody

    @GET("planetary/apod")
    suspend fun getImageOfTheDayAsync(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): PictureOfDay
}

// create calling api as local because expensive
object AsteroidApi {
    val retrofitService : AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}

