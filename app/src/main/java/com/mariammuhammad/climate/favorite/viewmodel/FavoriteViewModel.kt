package com.mariammuhammad.climate.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
//import com.google.android.libraries.places.api.net.PlacesClient
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: WeatherRepository) : ViewModel() {

    // StateFlow to represent the list of favorite cities wrapped in a Response
    val _favoriteCities = MutableStateFlow<Response<List<City>>>(Response.Loading)
    val favoriteCities: StateFlow<Response<List<City>>> = _favoriteCities

    private val _searchPlaceCoordinates = MutableStateFlow<Response<LatLng>>(Response.Loading)
    val searchPlaceCoordinates: StateFlow<Response<LatLng>> = _searchPlaceCoordinates


    // To initialize favoriteCities, we make the first fetch and handle success/failure
//    init {
//        fetchFavoriteCities()
//    }

    private fun fetchFavoriteCities() {
        viewModelScope.launch {
            _favoriteCities.value = Response.Loading // Set Loading state

            try {
                repository.getFavCitiesLocal().collect { cities ->
                    _favoriteCities.value = Response.Success(cities) // Set Success state with the list of cities
                }
            } catch (e: Exception) {
                _favoriteCities.value = Response.Failure(e) // Handle failure using Response.Failure
            }
        }
    }

    fun addFavoriteCity(city: City) {
        viewModelScope.launch {
            try {
                repository.insertFavCityLocal(city)

                // Update the state by adding the new city to the list
                val currentCities = (_favoriteCities.value as? Response.Success)?.data ?: emptyList()
                _favoriteCities.value = Response.Success(currentCities + city)
            } catch (e: Exception) {
                _favoriteCities.value = Response.Failure(e) // Handle failure using Response.Failure
            }
        }
    }

    fun removeFavoriteCity(city: City) {
        viewModelScope.launch {
            try {
                repository.deleteFavCityLocal(city)
                // Update the state by removing the city from the list
                val currentCities = (_favoriteCities.value as? Response.Success)?.data ?: emptyList()
                _favoriteCities.value = Response.Success(currentCities - city)
            } catch (e: Exception) {
                _favoriteCities.value = Response.Failure(e)
            }
        }
    }


    fun getLocalFavCities() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getFavCitiesLocal()
                    .catch { exception ->
                        // Handle failure by emitting Response.Failure
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
