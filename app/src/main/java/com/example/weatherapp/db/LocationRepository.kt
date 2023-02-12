package com.example.weatherapp.db

import androidx.annotation.WorkerThread
import com.example.weatherapp.models.Location

class LocationRepository(private val locationDao: LocationDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun allLocations():MutableList<Location>{
        return locationDao.getListOfSavedLocations()
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addLocation(location: Location) {
        locationDao.addLocation(location)
    }
}