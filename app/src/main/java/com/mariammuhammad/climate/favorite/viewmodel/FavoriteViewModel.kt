package com.mariammuhammad.climate.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: WeatherRepository) : ViewModel() {

    // StateFlow to represent the list of favorite cities wrapped in a Response
    private val _favoriteCities = MutableStateFlow<Response<List<City>>>(Response.Loading)
    val favoriteCities: StateFlow<Response<List<City>>> = _favoriteCities

    // To initialize favoriteCities, we make the first fetch and handle success/failure
    init {
        fetchFavoriteCities()
    }

    private fun fetchFavoriteCities() {
        viewModelScope.launch {
            _favoriteCities.value = Response.Loading // Set Loading state

            try {
                repository.getFavCitiesLocal().collect { cities ->
                    _favoriteCities.value = Response.Success(cities) // Set Success state with the list of cities
                }
            } catch (e: Exception) {
                _favoriteCities.value = Response.Failure(e) 
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
                _favoriteCities.value = Response.Failure(e) // Handle failure
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
}
/*
class FavoriteViewModel(private val repository: WeatherRepository) : ViewModel() {


    // StateFlow to represent the list of favorite cities wrapped in a Response
    private val _favoriteCities = MutableStateFlow<Response<List<City>>>(Response.Loading)
    val favoriteCities: StateFlow<Response<List<City>>> = _favoriteCities

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
                _favoriteCities.value = Response.Failure(e) // Set Failure state if an error occurs
            }
        }
    }

    fun addFavoriteCity(city: City) {
        viewModelScope.launch {
            try {
                repository.insertFavCityLocal(city)
                fetchFavoriteCities() // Re-fetch after adding a new city
            } catch (e: Exception) {
                _favoriteCities.value = Response.Failure(e) // Handle failure
            }
        }
    }

    fun removeFavoriteCity(city: City) {
        viewModelScope.launch {
            try {
                repository.deleteFavCityLocal(city)
                fetchFavoriteCities() // Re-fetch after removing a city
            } catch (e: Exception) {
                _favoriteCities.value = Response.Failure(e)
            }
        }
    }
}

*/