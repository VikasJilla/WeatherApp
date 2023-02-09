package com.example.weatherapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.models.Location
import com.example.weatherapp.models.Weather
import com.example.weatherapp.statemodels.InitialUIState
import com.example.weatherapp.statemodels.ProgressState
import com.example.weatherapp.statemodels.ResultUIState
import com.example.weatherapp.statemodels.UIState
import com.example.weatherapp.viewmodels.WeatherViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    private lateinit var activityMainBinding: ActivityMainBinding
    private val weatherViewModel : WeatherViewModel by viewModels()

    private lateinit var resultLauncher:ActivityResultLauncher<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        activityMainBinding.addLocation.setOnClickListener { launchSearch(it) }
        resultLauncher = registerForActivityResult(PickLocation()){
            Log.d(tag,"in registerForActivityResult callback")
            if(it != null){
                weatherViewModel.fetchWeather(it)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                weatherViewModel.uiState.collect {
                    handleStateChange(it)
                }
            }
        }
    }

    private fun handleStateChange(it: UIState) {
        when (it){
            is InitialUIState -> Log.d(tag,"in Initial UI State")
            is ProgressState -> {
                activityMainBinding.noContent.visibility = View.GONE
                activityMainBinding.resultTextView.visibility = View.GONE
                activityMainBinding.progressCircular.visibility = View.VISIBLE
            }
            is ResultUIState -> {
                activityMainBinding.progressCircular.visibility = View.GONE
                activityMainBinding.noContent.visibility = View.GONE
                activityMainBinding.resultTextView.visibility = View.VISIBLE
                if(it.isSuccess){
                    activityMainBinding.resultTextView.text = "Temperature: ${(it.data as Weather).currentWeather.temperature}"
                }else{
                    activityMainBinding.resultTextView.text = it.data as String
                }
            }
        }
    }

    private fun launchSearch(view: View) {
        resultLauncher.launch("")
    }
}

class PickLocation: ActivityResultContract<Any, Location?>() {
    override fun createIntent(context: Context, input: Any): Intent =
         Intent(context, SearchResultsActivity::class.java)


    override fun parseResult(resultCode: Int, intent: Intent?): Location? {
        Log.d("parseResult","$resultCode in parseResult")
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return if (Build.VERSION.SDK_INT >= 33) {
            intent?.getParcelableExtra("location",Location::class.java)
        } else if (Build.VERSION.SDK_INT < 33){
            intent?.getParcelableExtra<Location?>("location")
        }else{
             null
        }
    }

}