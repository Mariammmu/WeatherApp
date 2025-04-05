package com.mariammuhammad.climate.model.remote

import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.NextDaysWeather
import com.mariammuhammad.climate.utiles.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
//api.openweathermap.org/data/2.5/weather?q=London&appid=d8624de82ec00c03cb20e4952badd072
//api.openweathermap.org/data/2.5/forecast?q=cairo&appid=d8624de82ec00c03cb20e4952badd072

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") temperature: String,
        @Query("lang") language: String,
        @Query("appid") apiKey: String= Constants.ApiKey
        ): CurrentWeather

    @GET("forecast")
    suspend fun getWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid: String = "d8624de82ec00c03cb20e4952badd072"

    ):  Response<CurrentWeather>

    @GET("forecast")
    suspend fun get5DaysEvery3Hours(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") temperature: String,
        @Query("lang") language: String,
        @Query("appid") apiKey: String= Constants.ApiKey
        ): NextDaysWeather

}


