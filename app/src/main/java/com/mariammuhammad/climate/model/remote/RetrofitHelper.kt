package com.mariammuhammad.climate.model.remote

import com.mariammuhammad.climate.utiles.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    // Create a logging interceptor to log the HTTP request and response
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }// Log full request/response body

    // Create an OkHttpClient and add the logging interceptor to it
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retroFitInstance = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()



    val weatherService = retroFitInstance.create(WeatherService::class.java)
}