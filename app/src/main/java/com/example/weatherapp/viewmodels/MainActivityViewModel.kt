package com.example.weatherapp.viewmodels

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.db.LocationRepository
import com.example.weatherapp.models.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(private val repository: LocationRepository) : ViewModel() {
    fun saveLocationToDB(location: Location) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repository.addLocation(location)
        }
    }

    suspend fun getLocations():MutableList<Location>{
        var locs : MutableList<Location> = mutableListOf()
//        viewModelScope.launch {
            locs = withContext(Dispatchers.IO) {
                repository.allLocations()
            }
            Log.d("MainActivityViewModel","location length ${locs.size}")
//        }
        Log.d("MainActivityViewModel","location length ${locs.size}")
        return locs
    }
}

class MainActivityViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}