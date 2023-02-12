package com.example.weatherapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Location constructor(
    @PrimaryKey
    val id:Int,val name:String,val latitude:Double,val longitude:Double,val country:String,val admin1:String?) : Parcelable{
    fun displayText():String{
        return "$name, $admin1, $country"
    }
}