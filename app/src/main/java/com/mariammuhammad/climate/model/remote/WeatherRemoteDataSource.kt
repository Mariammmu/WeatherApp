package com.mariammuhammad.climate.model.remote

import android.util.Log
import com.google.gson.Gson
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSource(private val service: WeatherService) {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        tempUnit: String,
        lang: String
    ): Flow<CurrentWeather> = flow{
        emit(service.getWeatherData(lat, lon, tempUnit, lang))

    }

    suspend fun get5DaysEvery3Hours(
        lat: Double,
        lon: Double,
        tempUnit: String,
        lang: String
    ): Flow<NextDaysWeather> = flow {
        emit(service.get5DaysEvery3Hours(lat, lon, tempUnit, lang))
        Log.i("TAG", "get5DaysEvery3Hours:${Gson().toJson(service.get5DaysEvery3Hours(lat, lon, tempUnit, lang))} ")
    }
}