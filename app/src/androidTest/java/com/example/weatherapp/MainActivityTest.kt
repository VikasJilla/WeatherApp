package com.example.weatherapp

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import org.junit.Rule
import org.junit.Test

class MainActivityTest{

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()//To launch the main activity

    @Test
    fun testLaunchingLocationsSearchScreen(){
        onView(withId(R.id.add_location)).perform(click())
        onView(withId(R.id.locations_list)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}