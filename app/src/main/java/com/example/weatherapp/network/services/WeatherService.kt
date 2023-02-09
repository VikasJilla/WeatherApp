package com.example.weatherapp.network.services

import com.example.weatherapp.models.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun getForecast(
        @Query("longitude")longitude:Double,
        @Query("latitude")latitude:Double,
        @Query("current_weather") currentWeather:Boolean = true
    ):Response<Weather>
}