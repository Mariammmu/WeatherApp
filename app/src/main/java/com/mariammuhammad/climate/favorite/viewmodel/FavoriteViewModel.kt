package com.mariammuhammad.climate.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
//import com.google.android.libraries.places.api.net.PlacesClient
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: WeatherRepository) : ViewModel() {

    val _favoriteCities = MutableStateFlow<Response<List<City>>>(Response.Loading)
    val favoriteCities: StateFlow<Response<List<City>>> = _favoriteCities

    private val _searchPlaceCoordinates = MutableStateFlow<Response<LatLng>>(Response.Loading)
    val searchPlaceCoordinates: StateFlow<Response<LatLng>> = _searchPlaceCoordinates

    private val _favCityCurrentWeather = MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val favCityCurrentWeather =_favCityCurrentWeather.asStateFlow()

    private val _fiveDayFavCityWeather = MutableStateFlow<Response<NextDaysWeather>>(Response.Loading)
    val fiveDayFavoriteCity = _fiveDayFavCityWeather.asStateFlow()


    fun getRemoteFavCityCurrentWeather(lat: Double, lon: Double, units: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getWeatherForecast(lat, lon, units, lang)
                    .collect { response ->
                        _favCityCurrentWeather.value = Response.Success(response)
                    }
            } catch (e: Exception) {
                _favCityCurrentWeather.value = Response.Failure(e)
            }
        }
    }

    fun getRemote5Days3HoursWeather(lat: Double, lon: Double, units: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.get5DaysEvery3Hours(lat, lon, units, lang)
                    .collect { response ->
                        _fiveDayFavCityWeather.value = Response.Success(response)
                    }
            } catch (e: Exception) {
                _fiveDayFavCityWeather.value = Response.Failure(e)
            }
        }
    }

    fun addFavoriteCity(city: City) {
        viewModelScope.launch {
            try {
                repository.addFavCity(city)

                val currentCities = (_favoriteCities.value as? Response.Success)?.data ?: emptyList()
                _favoriteCities.value = Response.Success(currentCities + city)
            } catch (e: Exception) {
                _favoriteCities.value = Response.Failure(e)
            }
        }
    }

    fun removeFavoriteCity(city: City) {
        viewModelScope.launch {
            try {
                repository.deleteFavCity(city)
                val currentCities = (_favoriteCities.value as? Response.Success)?.data ?: emptyList()
                _favoriteCities.value = Response.Success(currentCities - city)
            } catch (e: Exception) {
                _favoriteCities.value = Response.Failure(e)
            }
        }
    }


    fun getFavCities() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getAllFavCities()
                    .catch { exception ->
                        _favoriteCities.value = Response.Failure(exception)
                    }
                    .collect { cities ->
                        _favoriteCities.value = Response.Success(cities)
                    }
            } catch (e: Exception) {
                // Handle error explicitly with Response.Failure
                _favoriteCities.value = Response.Failure(e)
            }
        }
    }

 //  /*
   fun getPlaceOnMap(searchText: String, placesClient: PlacesClient) {
        viewModelScope.launch {
            try {
                repository.getPlaceOnMap(searchText, placesClient)
                    .catch { exception ->
                        // Handle failure by emitting Response.Failure
                        _searchPlaceCoordinates.value = Response.Failure(exception)
                    }
                    .collect { coordinates ->
                        _searchPlaceCoordinates.value = Response.Success(coordinates) // On success
                    }
            } catch (e: Exception) {
                _searchPlaceCoordinates.value = Response.Failure(e)
            }
        }
    }
   // */
}

class FavoriteViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
