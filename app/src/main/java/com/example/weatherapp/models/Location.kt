package com.example.weatherapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location constructor(val id:Int,val name:String,val latitude:Double,val longitude:Double,val country:String,val admin1:String?) : Parcelable{
    fun displayText():String{
        return "$name, $admin1, $country"
    }
}