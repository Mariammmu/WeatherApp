package com.mariammuhammad.climate.model.remote

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
//        return try {
//            val currentWeatherResult =
//                service.getWeatherData(lat, lon, apiKey, tempUnit, lang)
//            if (currentWeatherResult != null) {
//                flowOf(Response.Success(currentWeatherResult))
//            } else {
//                flowOf(Response.Failure(Throwable("Error retrieving data")))
//            }
//        } catch (th: Throwable) {
//            flowOf(Response.Failure(th))
//        }
    }

    suspend fun get5DaysEvery3Hours(
        lat: Double,
        lon: Double,
        tempUnit: String,
        lang: String
    ): Flow<NextDaysWeather> = flow {
        emit(service.get5DaysEvery3Hours(lat, lon, tempUnit, lang))
//        return try {
//            val NextDaysWeather =
//                service.get5DaysEvery3Hours(lat, lon, apiKey, tempUnit, lang)
//            if (NextDaysWeather != null) {
//                flowOf( Response.Success(NextDaysWeather))
//            } else
//               flowOf(Response.Failure(Throwable("Error retrieving data")))
//        } catch (th: Throwable) {
//            flowOf( Response.Failure(th)) //without out        }
//        }
    }
}