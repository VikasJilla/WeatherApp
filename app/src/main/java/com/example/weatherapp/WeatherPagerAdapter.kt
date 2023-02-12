package com.example.weatherapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.fragments.WeatherFragment
import com.example.weatherapp.models.Location


class WeatherPagerAdapter(fragmentActivity: FragmentActivity,private var locations:MutableList<Location>) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return locations.size
    }

    override fun createFragment(position: Int): Fragment {
        return WeatherFragment(locations[position])
    }

    fun updateLocations(cLocations: MutableList<Location>){
        locations.addAll(cLocations)
        notifyDataSetChanged()
    }

    fun insertLocation(location: Location){
        locations.add(0,location)
        notifyItemInserted(0)
    }



}

