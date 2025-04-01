package com.mariammuhammad.climate.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.utiles.LocationUpdate
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel( val repo: WeatherRepository) : ViewModel() {

    private val _currentWeather =
        MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val currentWeather: StateFlow<Response<CurrentWeather>> = _currentWeather  //search about the *


    private val _nextDaysWeather = MutableStateFlow<Response<NextDaysWeather>>(Response.Loading)
    val nextDaysWeather: StateFlow<Response<NextDaysWeather>> = _nextDaysWeather


     fun getCurrentWeather(lat: Double, lon: Double, units: String, lang: String) {
         Log.i("TAG", "getCurrentWeather: ")
        viewModelScope.launch {

            repo.getWeatherForecast(lat, lon, units, lang).collect { response ->
                try {//(response is Response.Success) {
                    _currentWeather.value =  Response.Success(response) //as Response<CurrentWeather>  //not the better thing to do
                } catch(th: Throwable) {
                    _currentWeather.value =
                        Response.Failure(Throwable("Error retrieving weather data"))
                }
            }
        }
    }

     fun get5DaysWeather(lat: Double, lon: Double, tempUnit: String, lang: String) {
         Log.i("TAG", "get5DaysWeatherViewModelScope: Success ")

         viewModelScope.launch {
            repo.get5DaysEvery3Hours(lat, lon, tempUnit, lang).collect { response ->
                try {
                    Log.i("TAG", "get5DaysWeatherViewModel: Success ")

                    _nextDaysWeather.value = Response.Success(response)
                } catch (th: Throwable) {
                    Log.i("TAG", "get5DaysWeatherViewModel: Failure ")

                    _nextDaysWeather.value =
                        Response.Failure(Throwable("Error retrieving 5-day forecast"))
                }
            }
        }
    }
}



class WeatherFactory(private val _repo: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}