package com.example.weatherapp.network

import com.example.weatherapp.network.interceptors.HTTPLogInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private const val BASE_URL = "https://geocoding-api.open-meteo.com/v1/"
    private const val BASE_URL2 = "https://api.open-meteo.com/v1/"

    private val okHttpClient:OkHttpClient = OkHttpClient().newBuilder().apply {
        addInterceptor(HTTPLogInterceptor())
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofit2 = Retrofit.Builder()
        .baseUrl(BASE_URL2)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRetrofit():Retrofit {
        return retrofit
    }

    fun getRetrofit2():Retrofit {
        return retrofit2
    }

}