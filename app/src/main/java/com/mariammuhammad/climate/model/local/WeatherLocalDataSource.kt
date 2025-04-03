package com.mariammuhammad.climate.model.local

import android.content.Context
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource (
    private val favoritesDao: FavoritesDao,
    private val weatherDao: WeatherDao) : IWeatherLocalDataSource {

    override fun getAllFavCities(): Flow<List<City>> {
        return favoritesDao.getFavCities()
    }

    override suspend fun addFavCity(city: City):Long{
        return favoritesDao.insertFavCity(city)
    }

    override suspend fun deleteFavCity(city: City):Int{
        return favoritesDao.deleteFavCity(city)
    }

     override fun getAllStoredWeather(): Flow<NextDaysWeather> {
        return weatherDao.getStoredWeather()
    }

     override suspend fun addCurrentWeather(weatherResponse: NextDaysWeather) {
        weatherDao.insertAllCurrentWeather(weatherResponse)
    }

     override suspend fun removeAllWeather() {
        weatherDao.deleteAllWeather()
    }

}


