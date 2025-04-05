package com.mariammuhammad.climate.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.NextDaysWeather
import com.mariammuhammad.climate.utiles.NetworkManager
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeViewModel(val repo: WeatherRepository) : ViewModel() {

    private val _currentWeather =
        MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val currentWeather: StateFlow<Response<CurrentWeather>> = _currentWeather  //search about the *


    private val _nextDaysWeather = MutableStateFlow<Response<NextDaysWeather>>(Response.Loading)
    val nextDaysWeather: StateFlow<Response<NextDaysWeather>> = _nextDaysWeather

    private val _storedCurrentWeather = MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val storedWeather: StateFlow<Response<CurrentWeather>> = _storedCurrentWeather

    private val _storedNextDaysWeather = MutableStateFlow<Response<NextDaysWeather>>(Response.Loading)
    val storedNextDaysWeather: StateFlow<Response<NextDaysWeather>> = _storedNextDaysWeather

    private val _showMessage = MutableStateFlow<String?>(null)
    val showMessage: StateFlow<String?> = _showMessage

    private lateinit var networkManager: NetworkManager

    fun initialize(context: Context) {
        networkManager = NetworkManager(context)
    }

    // Method 1: Load data with network connectivity check
    fun loadData(lat: Double, lon: Double, units: String, lang: String) {
        if (!::networkManager.isInitialized) {
            _currentWeather.value = Response.Failure(Throwable("ViewModel not initialized"))
            _nextDaysWeather.value = Response.Failure(Throwable("ViewModel not initialized"))
            return
        }

        viewModelScope.launch {
            _currentWeather.value = Response.Loading
            _nextDaysWeather.value = Response.Loading

            if (networkManager.isNetworkAvailable()) {
                try {
                    // data from network
                    val current = repo.getWeatherForecast(lat, lon, units, lang).first()
                    val forecast = repo.get5DaysEvery3Hours(lat, lon, units, lang).first()

                    // Update UI
                    _currentWeather.value = Response.Success(current)
                    _nextDaysWeather.value = Response.Success(forecast)

                    // Cache the data
                    cacheData(current, forecast)
                } catch (e: Exception) {
                    _showMessage.value = "Network error, loading cached data"
                    loadCachedData()
                }
            } else {
                _showMessage.value = "Offline, loading cached data"
                loadCachedData()
            }
        }
    }

    private fun cacheData(current: CurrentWeather, forecast: NextDaysWeather) {
        viewModelScope.launch {
            try {
                repo.insertCurrentWeather(current)
                repo.insertNextDaysWeather(forecast)
            } catch (e: Exception) {
                Log.e("Caching", "Failed to cache data", e)
            }
        }
    }

    // Helper method to load cached data
    private suspend fun loadCachedData() {
        try {
            // Get the latest cached data
            val cachedCurrent = repo.getAllCurrentWeatherFromRoom().firstOrNull()
            val cachedForecast = repo.getNextDaysWeatherFromRoom().firstOrNull()

            cachedCurrent?.let {
                _currentWeather.value = Response.Success(it)
            } ?: run {
                _currentWeather.value = Response.Failure(Throwable("No cached current weather"))
            }

            cachedForecast?.let {
                _nextDaysWeather.value = Response.Success(it)
            } ?: run {
                _nextDaysWeather.value = Response.Failure(Throwable("No cached forecast"))
            }
        } catch (e: Exception) {
            _currentWeather.value = Response.Failure(e)
            _nextDaysWeather.value = Response.Failure(e)
        }
    }

    fun messageShown() {
        _showMessage.value = null
    }

//    fun loadWeather(lat: Double, lon: Double, units: String, lang: String) {
//        if (!::networkManager.isInitialized) {
//            _nextDaysWeather.value = Response.Failure(Throwable("ViewModel not initialized"))
//            return
//        }
//
//        viewModelScope.launch {
//            _nextDaysWeather.value = Response.Loading
//
//            if (networkManager.isNetworkAvailable()) {
//                try {
//                    Log.i("TAG", "loadWeather: internet avail  ")
//
//                    repo.get5DaysEvery3Hours(lat, lon, units, lang).collect { response ->
//                        _nextDaysWeather.value = Response.Success(response)
//                        repo.insertCurrentWeather(response)
//
//                    }
//                } catch (e: Exception) {
//                    Log.i("TAG", "loadWeather: Internet error ")
//                    e.printStackTrace()
//                    loadFromCache()
//                    _showMessage.value = "Using cached data (API failed)"
//                }
//            } else {
//                Log.i("TAG", "loadWeather: No internet  ")
//                loadFromCache()
//                _showMessage.value = "No internet connection - showing cached data"
//            }
//        }
//    }
//
//    private fun loadFromCache() {
//        viewModelScope.launch (Dispatchers.IO) {
//            try {
//                Log.i("TAG", "loadFromCache: get ")
//                repo.getAllCurrentWeatherFromRoom().collect { cachedData ->
//                        Log.i("TAG", "loadFromCache: ${cachedData}")
//                        _nextDaysWeather.value = Response.Success(cachedData)
//                }
//            } catch (e: Exception) {
//                Log.i("TAG", "loadFromCache: error retrieving data ")
//                e.printStackTrace()
//                    _nextDaysWeather.value = Response.Failure(e)
//
//            }
//        }
//    }

    fun getCurrentWeather(lat: Double, lon: Double, units: String, lang: String) {
        //Log.i("TAG", "getCurrentWeather: ")
        viewModelScope.launch {

            repo.getWeatherForecast(lat, lon, units, lang).collect { response ->
                try {//(response is Response.Success) {
                    _currentWeather.value =
                        Response.Success(response) //as Response<CurrentWeather>  //not the better thing to do
                } catch (th: Throwable) {
                    _currentWeather.value =
                        Response.Failure(Throwable("Error retrieving weather data"))
                }
            }
        }
    }

    fun get5DaysWeather(lat: Double, lon: Double, tempUnit: String, lang: String) {
        //  Log.i("TAG", "get5DaysWeatherViewModelScope: Success ")

        viewModelScope.launch {
            repo.get5DaysEvery3Hours(lat, lon, tempUnit, lang).collect { response ->
                try {
                    //    Log.i("TAG", "get5DaysWeatherViewModel: Success ")

                    _nextDaysWeather.value = Response.Success(response)
                } catch (th: Throwable) {
                    //  Log.i("TAG", "get5DaysWeatherViewModel: Failure ")

                    _nextDaysWeather.value =
                        Response.Failure(Throwable("Error retrieving 5-day forecast"))
                }
            }
        }
    }

    fun getStoredCurrentWeather() {
        viewModelScope.launch {
            repo.getAllCurrentWeatherFromRoom().collect { response ->
                try {
                    _storedCurrentWeather.value = Response.Success(response)
                } catch (th: Throwable) {
                    _storedCurrentWeather.value =
                        Response.Failure(Throwable("Error retrieving stored weather"))
                }
            }
        }
    }

    fun insertCurrentWeather(currentWeather: CurrentWeather) {
        viewModelScope.launch {
            try {
                repo.insertCurrentWeather(currentWeather)
            } catch (th: Throwable) {
                _storedCurrentWeather.value = Response.Failure(Throwable("Error inserting weather data"))
            }
        }

    }


fun getStoredNextDaysWeather() {
    viewModelScope.launch {
        repo.getNextDaysWeatherFromRoom().collect { response ->
            try {
                _storedNextDaysWeather.value = Response.Success(response)
            } catch (th: Throwable) {
                _storedNextDaysWeather.value =
                    Response.Failure(Throwable("Error retrieving stored weather"))
            }
        }
    }
}

fun insertNextDaysWeather(weatherResponse: NextDaysWeather) {
    viewModelScope.launch {
        try {
            repo.insertNextDaysWeather(weatherResponse)
        } catch (th: Throwable) {
            _storedNextDaysWeather.value = Response.Failure(Throwable("Error inserting weather data"))
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
