package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Weather(@SerializedName("current_weather") var currentWeather: CurrentWeather)

data class CurrentWeather(var temperature:Double)
