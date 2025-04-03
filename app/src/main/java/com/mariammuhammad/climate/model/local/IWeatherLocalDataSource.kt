package com.mariammuhammad.climate.model.local

import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    fun getAllFavCities(): Flow<List<City>>

    suspend fun addFavCity(city: City): Long
    suspend fun deleteFavCity(city: City): Int
    fun getAllStoredWeather(): Flow<NextDaysWeather>

    suspend fun addCurrentWeather(weatherResponse: NextDaysWeather)

    suspend fun removeAllWeather()
}