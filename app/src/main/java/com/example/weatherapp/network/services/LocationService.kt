package com.example.weatherapp.network.services

import com.example.weatherapp.models.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {
    @GET("search")
    suspend fun getLocations(@Query("name") city:String,@Query("count") count:Int = 10): Response<LocationResponse>
}