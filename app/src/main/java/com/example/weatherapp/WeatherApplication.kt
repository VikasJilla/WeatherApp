package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.LocationRepository

class WeatherApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val locationRepository by lazy { LocationRepository(database.getLocationDao())}
}