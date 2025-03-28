package com.mariammuhammad.climate.model

import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl (private val remoteDataSource: WeatherRemoteDataSource): WeatherRepository {
    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        units: String,
        lan: String
    ): Flow<CurrentWeather>{
        return remoteDataSource.getCurrentWeather(lat,lon,units,lan)
    }

    override suspend fun get5DaysEvery3Hours(
        lat: Double,
        lon: Double,
        units: String,
        lan: String
    ): Flow<NextDaysWeather> {
        return remoteDataSource.get5DaysEvery3Hours(lat, lon, units, lan)
    }
}
