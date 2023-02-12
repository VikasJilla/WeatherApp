package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Location
import com.example.weatherapp.network.RetrofitProvider
import com.example.weatherapp.network.services.WeatherService
import com.example.weatherapp.statemodels.InitialUIState
import com.example.weatherapp.statemodels.ProgressState
import com.example.weatherapp.statemodels.ResultUIState
import com.example.weatherapp.statemodels.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherFragmentViewModel:ViewModel() {
    private val state: MutableStateFlow<UIState> = MutableStateFlow(InitialUIState())
    val uiState: StateFlow<UIState> = state.asStateFlow()

    fun fetchWeather(location: Location){
        state.update {
            ProgressState()
        }
        viewModelScope.launch {
            val weatherResponse = RetrofitProvider.getRetrofit2().create(WeatherService::class.java).getForecast(location.longitude,location.latitude)
            state.update {
                ResultUIState(weatherResponse.isSuccessful,if(weatherResponse.isSuccessful)weatherResponse.body() else "Could not fetch the weather data")
            }
        }
    }
}