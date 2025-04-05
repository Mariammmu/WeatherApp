package com.mariammuhammad.climate.model.local

import com.mariammuhammad.climate.model.data.Alarm
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.NextDaysWeather
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    fun getAllFavCities(): Flow<List<City>>
    suspend fun addFavCity(city: City): Long
    suspend fun deleteFavCity(city: City): Int

    fun getAllStoredWeather(): Flow<NextDaysWeather>
    suspend fun addNextDaysWeather(weatherResponse: NextDaysWeather)

    fun getCurrentWeather(): Flow<CurrentWeather>
    suspend fun addCurrentWeather(currentWeather: CurrentWeather)

    fun getAllAlarms():Flow<List<Alarm>>
    suspend fun addAlarm(alarm: Alarm):Long
    suspend fun deleteAlarm(alarm:Alarm):Int
//    suspend fun deleteAlarmById(alarmId:Int)

    }