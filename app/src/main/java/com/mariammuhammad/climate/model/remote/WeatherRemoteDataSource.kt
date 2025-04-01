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

     fun getCurrentWeather(
        lat: Double,
        lon: Double,
        tempUnit: String,
        lang: String
    ): Flow<CurrentWeather> = flow{
        emit(service.getWeatherData(lat, lon, tempUnit, lang))

    }

     fun get5DaysEvery3Hours(
        lat: Double,
        lon: Double,
        tempUnit: String,
        lang: String
    ): Flow<NextDaysWeather> = flow {
        emit(service.get5DaysEvery3Hours(lat, lon, tempUnit, lang))

        //emit vs flowOf
        Log.i("TAG", "get5DaysEvery3Hours:${Gson().toJson(service.get5DaysEvery3Hours(lat, lon, tempUnit, lang))} ")
    }

//    suspend fun getPlaceOnMap(searchText: String, placesClient: PlacesClient) : Flow<LatLng>{
//
//        var placeCoordinates : LatLng = LatLng(20.0,20.0)
//        // 1. Find predictions
//        val request = FindAutocompletePredictionsRequest.builder()
//            .setQuery(searchText)
//            .build()
//
//        placesClient.findAutocompletePredictions(request)
//            .addOnSuccessListener { response ->
//                // Take the first prediction
//                response.autocompletePredictions.firstOrNull()?.let { prediction ->
//                    // 2. Get place details
//                    val receivedInfo = listOf(Place.Field.LAT_LNG)
//                    val placeRequest = FetchPlaceRequest.builder(
//                        prediction.placeId,
//                        receivedInfo
//                    ).build()
//
//                    placesClient.fetchPlace(placeRequest)
//                        .addOnSuccessListener { placeResponse ->
//                            placeResponse.place.latLng?.let { latLng ->
//                                Log.i("TAG", "getPlaceOnMap: $latLng")
//                                placeCoordinates = latLng
//                            }
//                        }
//                }
//            }
//        delay(1000)
//        return flowOf(placeCoordinates)
//    }

}