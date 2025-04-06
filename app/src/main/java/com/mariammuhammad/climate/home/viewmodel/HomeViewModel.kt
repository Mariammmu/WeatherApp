package com.mariammuhammad.climate.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.NextDaysWeather
import com.mariammuhammad.climate.settings.data.ISettingsRepo
import com.mariammuhammad.climate.utiles.NetworkManager
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeViewModel(val repo: WeatherRepository, val settingsRepo: ISettingsRepo) : ViewModel() {

    private val _currentWeather =
        MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val currentWeather: StateFlow<Response<CurrentWeather>> = _currentWeather  //search about the *


    private val _nextDaysWeather = MutableStateFlow<Response<NextDaysWeather>>(Response.Loading)
    val nextDaysWeather: StateFlow<Response<NextDaysWeather>> = _nextDaysWeather ////toast/snackbar/

    private val _storedCurrentWeather = MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val storedCurrentWeather: StateFlow<Response<CurrentWeather>> = _storedCurrentWeather

    private val _storedNextDaysWeather = MutableStateFlow<Response<NextDaysWeather>>(Response.Loading)
    val storedNextDaysWeather: StateFlow<Response<NextDaysWeather>> = _storedNextDaysWeather

    private val _showMessage = MutableSharedFlow<Boolean>() //Flow<String?>(null) event action
    val showMessage: SharedFlow<Boolean?> = _showMessage

    private val _tempUnit = MutableStateFlow("")
    val tempUnit = _tempUnit.asStateFlow()

    private val _language = MutableStateFlow("")
    val language = _language.asStateFlow()

    private val _windUnit = MutableStateFlow("")
    val windUnit = _windUnit.asStateFlow()


    private val _locationFinder = MutableStateFlow("")
    val locationFinder = _locationFinder.asStateFlow()

    private val _savedLatitude = MutableStateFlow(0f)
    val savedLatitude = _savedLatitude.asStateFlow()

    private val _savedLongitude = MutableStateFlow(0f)
    val savedLongitude = _savedLongitude.asStateFlow()


    private lateinit var networkManager: NetworkManager

    fun initialize(context: Context) {
        networkManager = NetworkManager(context)
    }

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
                    val current = repo.getWeatherForecast(lat, lon, units, lang).first()
                    val forecast = repo.get5DaysEvery3Hours(lat, lon, units, lang).first()

                    // Update UI with fresh data
                    _currentWeather.value = Response.Success(current)
                    _nextDaysWeather.value = Response.Success(forecast)

                    viewModelScope.launch {
                        try {
                            repo.insertCurrentWeather(current)
                            repo.insertNextDaysWeather(forecast)
                        } catch (e: Exception) {
                            Log.e("Caching", "Failed to cache data", e)
                        }
                    }
                } catch (e: Exception) {
                    _showMessage.emit(true)

                    //_showMessage.value = "Network error, showing cached data"
                    loadCachedData(lat, lon)
                }
            }
            else {
                _showMessage.emit(true)
                        // "No internet connection, showing cached data"
                loadCachedData(lat, lon)
            }
        }
    }

    private suspend fun loadCachedData(lat: Double, lon: Double) {
        try {
            val cachedCurrent = repo.getAllCurrentWeatherFromRoom().firstOrNull()
            val cachedForecast = repo.getNextDaysWeatherFromRoom().firstOrNull()

            cachedCurrent?.let {
                _currentWeather.value = Response.Success(it)
            } ?: run {
                _currentWeather.value = Response.Failure(Throwable("No cached data available"))
            }

            cachedForecast?.let {
                _nextDaysWeather.value = Response.Success(it)
            } ?: run {
                _nextDaysWeather.value = Response.Failure(Throwable("No cached forecast available"))
            }
        } catch (e: Exception) {
            _currentWeather.value = Response.Failure(e)
            _nextDaysWeather.value = Response.Failure(e)
        }
    }

//    fun messageShown() {
//        _showMessage.value = null
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

//    fun getStoredCurrentWeather() {
//        viewModelScope.launch {
//            repo.getAllCurrentWeatherFromRoom().collect { response ->
//                try {
//                    _storedCurrentWeather.value = Response.Success(response)
//                } catch (th: Throwable) {
//                    _storedCurrentWeather.value =
//                        Response.Failure(Throwable("Error retrieving stored weather"))
//                }
//            }
//        }
//    }
//
//    fun insertCurrentWeather(currentWeather: CurrentWeather) {
//        viewModelScope.launch {
//            try {
//                repo.insertCurrentWeather(currentWeather)
//            } catch (th: Throwable) {
//                _storedCurrentWeather.value = Response.Failure(Throwable("Error inserting weather data"))
//            }
//        }
//
//    }


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

    fun getSavedSettings() {
        getMeasurementSystem()
        getLanguage()
        getLocationFinder()
        getSavedLocation()
        getWindUnit()
    }

    private fun getMeasurementSystem() {
        _tempUnit.value = settingsRepo.getTemperatureUnit()
    }

    private fun getWindUnit() {
        _windUnit.value = settingsRepo.getWindSpeedUnit()
    }


    private fun getLanguage() {
        _language.value = settingsRepo.getLanguage()
    }

    private fun getLocationFinder() {
        _locationFinder.value = settingsRepo.getLocationFinder()
    }


    private fun getSavedLocation() {
        _savedLatitude.value = settingsRepo.getLatitude()
        _savedLongitude.value = settingsRepo.getLongitude()
    }

//fun insertNextDaysWeather(weatherResponse: NextDaysWeather) {
//    viewModelScope.launch {
//        try {
//            repo.insertNextDaysWeather(weatherResponse)
//        } catch (th: Throwable) {
//            _storedNextDaysWeather.value = Response.Failure(Throwable("Error inserting weather data"))
//        }
//    }

}


class WeatherFactory(private val _repo: WeatherRepository,
                     private val settingRepo: ISettingsRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(_repo,settingRepo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }

    }
}
