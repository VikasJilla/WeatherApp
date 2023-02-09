package com.example.weatherapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.models.Location

class LocationsListAdapter(private var onClick: (Location) -> Unit,private var locationList:MutableList<Location>) :
    ListAdapter<Location, LocationViewHolder>(LocationDiffCallBack) {
    val tag = "LocationsListAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_list_item, parent, false)
        return LocationViewHolder(view,onClick)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        Log.d(tag,"setting name ${locationList[position].name}")
        holder.locationView.text = locationList[position].displayText()
        holder.locationView.setOnClickListener{
            onClick(locationList[position])
        }
    }

    override fun getItemCount(): Int {
        Log.d(tag,"size ${locationList.size}")
        return locationList.size
    }

    fun updateList(locations:List<Location>){
        Log.d(tag,"size updateList ${locations.size}")
        locationList.clear()
        locationList.addAll(locations)
        notifyDataSetChanged()
    }
}

class LocationViewHolder(itemView: View,var onClick: (Location) -> Unit) : RecyclerView.ViewHolder(itemView) {
    var locationView: TextView = itemView.findViewById(R.id.location_name_tv)
}

object LocationDiffCallBack : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }
}