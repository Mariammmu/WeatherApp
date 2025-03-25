package com.mariammuhammad.climate.model.remote

import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.WeatherItemRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") temperature: String,
        @Query("lang") language: String,


        ): Response<CurrentWeather> // which response we will use

    @GET("forecast")
    suspend fun getWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid: String = "d8624de82ec00c03cb20e4952badd072"

    ):  Response<CurrentWeather>

    @GET("forecast")
    suspend fun get5DaysEach3Hours(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") temperature: String,
        @Query("lang") language: String,
    ): Response<WeatherItemRoot>

}


