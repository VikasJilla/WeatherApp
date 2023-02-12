package com.example.weatherapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.models.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location")
    fun getListOfSavedLocations(): MutableList<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocation(location: Location)
}