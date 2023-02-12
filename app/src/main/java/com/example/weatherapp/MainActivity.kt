package com.example.weatherapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.models.Location
import com.example.weatherapp.viewmodels.MainActivityViewModel
import com.example.weatherapp.viewmodels.MainActivityViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    private lateinit var adapter:WeatherPagerAdapter

    private lateinit var activityMainBinding: ActivityMainBinding
    private val mainActivityViewModel : MainActivityViewModel by viewModels{
        MainActivityViewModelFactory((application as WeatherApplication).locationRepository)
    }

    private lateinit var resultLauncher:ActivityResultLauncher<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        supportActionBar?.hide()


        activityMainBinding.addLocation.setOnClickListener { launchSearch(it) }
        activityMainBinding.floatingActionButton.setOnClickListener { launchSearch(it) }

        resultLauncher = registerForActivityResult(PickLocation()){
            Log.d(tag,"in registerForActivityResult callback")
            if(it != null){
                mainActivityViewModel.saveLocationToDB(it)
                adapter.insertLocation(it)
                activityMainBinding.viewpager.postDelayed(Runnable { activityMainBinding.viewpager.currentItem = 0 }, 100)
                if(activityMainBinding.viewpager.visibility != View.VISIBLE){
                    updateUI(false)
                }
            }
        }
        adapter = WeatherPagerAdapter(this, mutableListOf())
        activityMainBinding.viewpager.adapter = adapter
        lifecycleScope.launch {
            val locations = mainActivityViewModel.getLocations()
            Log.d(tag,"locations length ${locations.size}")
            updateUI(locations.isEmpty())
            adapter.updateLocations(locations)
        }
    }

    private fun updateUI(isEmpty:Boolean){
        activityMainBinding.noContent.visibility = if(isEmpty)View.VISIBLE else View.GONE
        activityMainBinding.viewpager.visibility = if(isEmpty)View.GONE else View.VISIBLE
        activityMainBinding.floatingActionButton.visibility = if(isEmpty)View.GONE else View.VISIBLE
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