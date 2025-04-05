package com.mariammuhammad.climate.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.mariammuhammad.climate.model.data.Alarm
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.NextDaysWeather
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


    fun getAllFavCities():Flow<List<City>>

    suspend fun addFavCity(city: City):Long

    suspend fun deleteFavCity(city: City):Int

    suspend fun getLocationOnMap(searchText:String,placesClient: PlacesClient) :Flow<LatLng>


    fun getNextDaysWeatherFromRoom(): Flow<NextDaysWeather>

    suspend fun insertNextDaysWeather(weatherResponse: NextDaysWeather)

    fun getAllCurrentWeatherFromRoom(): Flow<CurrentWeather>

    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)



    fun getAlarms():Flow<List<Alarm>>

    suspend fun insertAlarm(alarm: Alarm):Long

    suspend fun deleteAlarm(alarm: Alarm):Int

//    suspend fun deleteAlarmById(alarmId:Int)
    }