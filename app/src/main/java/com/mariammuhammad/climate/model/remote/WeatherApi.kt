package com.mariammuhammad.climate.model.remote

import com.mariammuhammad.climate.model.pojo.ListItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") temperature: String,
        @Query("lang") language: String,


        ): Response<WeatherResponse> // which response we will use

    @GET("forecast")
    suspend fun get5DaysEach3Hours(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") temperature: String,
        @Query("lang") language: String,
    ): Response<DetailsResponse>

}


