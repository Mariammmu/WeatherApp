package com.mariammuhammad.climate.model.remote

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
//import com.google.android.libraries.places.api.net.PlacesClient

import com.google.gson.Gson
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.delay
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
         Log.i("TAG", "get5DaysEvery3Hours:${Gson().toJson(service.get5DaysEvery3Hours(lat, lon, tempUnit, lang))} ")
    }

    suspend fun getLocationOnMap(searchText: String, placesClient: PlacesClient) : Flow<LatLng>{

        var placeCoordinates = LatLng(20.0,20.0)

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(searchText)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                response.autocompletePredictions.firstOrNull()?.let { prediction ->

                    //details
                    val receivedInfo = listOf(Place.Field.LAT_LNG)
                    val placeRequest = FetchPlaceRequest.builder(
                        prediction.placeId,
                        receivedInfo
                    ).build()

                    placesClient.fetchPlace(placeRequest)
                        .addOnSuccessListener { placeResponse ->
                            placeResponse.place.latLng?.let { latLng ->
                                placeCoordinates = latLng
                            }
                        }
                }
            }
        delay(1000)
        return flowOf(placeCoordinates)
    }

}