package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.WeatherLayoutBinding
import com.example.weatherapp.models.Location
import com.example.weatherapp.models.Weather
import com.example.weatherapp.statemodels.InitialUIState
import com.example.weatherapp.statemodels.ProgressState
import com.example.weatherapp.statemodels.ResultUIState
import com.example.weatherapp.statemodels.UIState
import com.example.weatherapp.viewmodels.WeatherFragmentViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WeatherFragment(private var location: Location): Fragment() {

    private lateinit var weatherLayoutBinding: WeatherLayoutBinding
    private val weatherFragmentViewModel: WeatherFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherLayoutBinding = WeatherLayoutBinding.inflate(inflater)
        lifecycleScope.launch {
            weatherFragmentViewModel.uiState.collect{
                handleStateChange(it)
            }
        }
        return weatherLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        weatherFragmentViewModel.fetchWeather(location)
        Log.d(tag,"${location.name} in onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    private fun handleStateChange(it: UIState) {
        when (it){
            is InitialUIState -> Log.d(tag,"in Initial UI State")
            is ProgressState -> {

            }
            is ResultUIState -> {
                if(it.isSuccess){
                    weatherLayoutBinding.locationName.text = "${location.name}, ${location.admin1}, ${location.country}"
                    weatherLayoutBinding.temperatureText.text = "Temperature: ${(it.data as Weather).currentWeather.temperature}"
                }else{
                    weatherLayoutBinding.temperatureText.text = it.data as String
                }
            }
        }
    }
}