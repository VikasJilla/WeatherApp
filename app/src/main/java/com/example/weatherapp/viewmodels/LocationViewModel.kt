package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.RetrofitProvider
import com.example.weatherapp.network.services.LocationService
import com.example.weatherapp.statemodels.InitialUIState
import com.example.weatherapp.statemodels.ProgressState
import com.example.weatherapp.statemodels.ResultUIState
import com.example.weatherapp.statemodels.UIState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private var state: MutableStateFlow<UIState> = MutableStateFlow(InitialUIState())

    val uiState: StateFlow<UIState> = state.asStateFlow()

    fun getLocations(searchText: String?) {
        state.update {
            ProgressState()
        }
        val exceptionHandler = CoroutineExceptionHandler() { ctx, t ->
            Log.d("", "caught $ctx, $t")
            state.update {
                ResultUIState(false, t.localizedMessage)
            }
        }

        viewModelScope.launch(exceptionHandler) {
            val locationResponse =
                RetrofitProvider.getRetrofit().create(LocationService::class.java)
                    .getLocations(searchText.orEmpty())
            Log.d("LocationViewModel", "size: ${locationResponse.body()?.results?.size ?: 0}")
            if (locationResponse.isSuccessful) {
                val list = locationResponse.body()?.results.orEmpty()
                state.update {
                    ResultUIState(list.isNotEmpty(), list.ifEmpty { "No Locations Found!" })
                }
            } else {
                state.update {
                    ResultUIState(false, locationResponse.errorBody().toString())
                }
            }

        }
    }
}