package com.mariammuhammad.climate.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.mariammuhammad.climate.model.pojo.City
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

    fun getAllFavCities():Flow<List<City>>

    suspend fun addFavCity(city: City):Long


    suspend fun deleteFavCity(city: City):Int

    suspend fun getPlaceOnMap(searchText:String,placesClient: PlacesClient) :Flow<LatLng>

    fun getAllCurrentWeatherFromRoom(): Flow<NextDaysWeather>

    suspend fun insertCurrentWeather(weatherResponse: NextDaysWeather)

    suspend fun deleteStoredCurrentWeather()

    }