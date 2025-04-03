package com.mariammuhammad.climate.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.mariammuhammad.climate.model.local.WeatherLocalDataSource
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl (private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource): WeatherRepository {
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

    override fun getFavCitiesLocal():Flow<List<City>>{
        return localDataSource.getFavCitiesLocal()
    }

    override suspend fun insertFavCityLocal(city: City):Long{
        return localDataSource.insertFavCityLocal(city)
    }

    override suspend fun deleteFavCityLocal(city: City):Int{
        return localDataSource.deleteFavCityLocal(city)
    }

    override suspend fun getPlaceOnMap(searchText:String, placesClient: PlacesClient) :Flow<LatLng>{
        return remoteDataSource.getPlaceOnMap(searchText,placesClient)
       }
}
