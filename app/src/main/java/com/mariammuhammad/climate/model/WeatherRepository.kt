package com.mariammuhammad.climate.model

import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        units: String,
        lan: String
    ): Flow<CurrentWeather>

    suspend fun get5DaysEvery3Hours(
        lat: Double,
        lon: Double,
        units: String,
        lan: String
    ): Flow<NextDaysWeather>


}