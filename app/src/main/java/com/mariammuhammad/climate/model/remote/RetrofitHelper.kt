package com.mariammuhammad.climate.model.remote

import com.mariammuhammad.climate.utiles.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retroFitInstance = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherService = retroFitInstance.create(WeatherService::class.java)
}