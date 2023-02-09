package com.example.weatherapp

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.ActivitySearchResultsBinding
import com.example.weatherapp.models.Location
import com.example.weatherapp.models.LocationResponse
import com.example.weatherapp.network.RetrofitProvider
import com.example.weatherapp.network.services.LocationService
import com.example.weatherapp.statemodels.InitialUIState
import com.example.weatherapp.statemodels.ProgressState
import com.example.weatherapp.statemodels.ResultUIState
import com.example.weatherapp.statemodels.UIState
import com.example.weatherapp.utilities.UIUtils
import com.example.weatherapp.utilities.UIUtils.Companion.hideKeyboard
import com.example.weatherapp.viewmodels.LocationViewModel
import kotlinx.coroutines.*

class SearchResultsActivity : AppCompatActivity() {

    val tag = "SearchResultsActivity"
    val locationViewModel: LocationViewModel by viewModels()
    private lateinit var activityBinding: ActivitySearchResultsBinding
    lateinit var locationsListAdapter: LocationsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                locationViewModel.uiState.collect {
                    handleStateChange(it)
                }
            }
        }
        locationsListAdapter = LocationsListAdapter(::onLocationClick,  mutableListOf())
        activityBinding.locationsList.adapter = locationsListAdapter
        activityBinding.locationsList.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
    }

    private fun onLocationClick(location: Location){
        Log.d(tag,"location selected ${location.name}")
        val intent = Intent();
        intent.putExtra("location",location)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    private fun handleStateChange(it:UIState){
        when(it){
            is ProgressState -> {
                activityBinding.progressCircular.visibility = View.VISIBLE
                activityBinding.errorLayout.visibility = View.GONE
                activityBinding.locationsList.visibility = View.GONE
            }
            is InitialUIState -> {
                activityBinding.errorLayout.visibility = View.GONE
                activityBinding.locationsList.visibility = View.GONE
                activityBinding.progressCircular.visibility = View.INVISIBLE
            }
            is ResultUIState ->{
                if(it.isSuccess){
                    Log.d(tag,"in isSuccess")
                    activityBinding.errorLayout.visibility = View.GONE
                    activityBinding.progressCircular.visibility = View.INVISIBLE
                    activityBinding.locationsList.visibility = View.VISIBLE
                    if(it.data is List<*>){
                        locationsListAdapter.updateList((it.data as List<*>).filterIsInstance<Location>())
                    }
                }else{
                    activityBinding.errorLayout.visibility = View.VISIBLE
                    activityBinding.locationsList.visibility = View.GONE
                    activityBinding.progressCircular.visibility = View.INVISIBLE
                    if(it.data is String){
                        activityBinding.errorMessage.text = it.data as String
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchViewItem = menu.findItem(R.id.menu_search)
//         Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView:SearchView = searchViewItem.actionView as SearchView
        searchView.apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = false
            isSubmitButtonEnabled = false
            queryHint = getString(R.string.search_hint)
        }
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(tag,"on query Text Submit $query")
                hideKeyboard()
                locationViewModel.getLocations(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }
}

