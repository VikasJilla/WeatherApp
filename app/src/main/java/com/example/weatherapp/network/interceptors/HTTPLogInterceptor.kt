package com.example.weatherapp.network.interceptors

import android.net.http.HttpResponseCache
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class HTTPLogInterceptor : Interceptor {
    val tag = "HTTPInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d(tag,"url: ${request.url}")
        Log.d(tag,"method: ${request.method}")
        Log.d(tag,"body: ${request.body}")
        val response = chain.proceed(request)
        when(response.code){
            in 200..211 -> Log.d(tag,"status success: ${response.code}")
            401 -> Log.d(tag,"Failure Unauthorized")
            404 -> Log.d(tag,"Failure - Page Not Found")
            else -> Log.d(tag,"Failure: ${response.code}")
        }
        return response
    }
}