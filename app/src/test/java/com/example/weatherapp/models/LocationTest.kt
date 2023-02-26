package com.example.weatherapp.models

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class LocationTest {
    @Test
    fun displayText() {
        //Arrange
        val location = Location(123,"Hyderabad",12.5,12.5,"India","Telangana")
        //Act
        val actual = location.displayText()
        //Assert
        assertEquals("Hyderabad, Telangana, India",actual)
    }
}