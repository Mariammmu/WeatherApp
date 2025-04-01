package com.mariammuhammad.climate.model.local

import android.content.Context
import com.mariammuhammad.climate.model.pojo.City
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource (private val favoritesDao: FavoritesDao) {

    fun getFavCitiesLocal(): Flow<List<City>> {
        return favoritesDao.getFavCities()
    }

    suspend fun insertFavCityLocal(city: City):Long{
        return favoritesDao.insertFavCity(city)
    }

    suspend fun deleteFavCityLocal(city: City):Int{
        return favoritesDao.deleteFavCity(city)
    }

}


