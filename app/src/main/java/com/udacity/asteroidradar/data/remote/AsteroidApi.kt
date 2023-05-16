package com.udacity.asteroidradar.data.remote

import com.udacity.asteroidradar.data.local.models.PictureOfDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApi {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsByDate(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): Response<String>

    @GET("planetary/apod")
    suspend fun getPictureOfDay(): Response<PictureOfDay>
}
