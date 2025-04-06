package com.mariammuhammad.climate.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mariammuhammad.climate.settings.data.SettingsRepo
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepo) : ViewModel() {

    private val _temperatureUnit = MutableStateFlow<Response<String>>(Response.Loading)
    val temperatureUnit: StateFlow<Response<String>> = _temperatureUnit

    private val _language = MutableStateFlow<Response<String>>(Response.Loading)
    val language: StateFlow<Response<String>> = _language

    private val _locationFinder = MutableStateFlow<Response<String>>(Response.Loading)
    val locationFinder: StateFlow<Response<String>> = _locationFinder

    private val _coordinates = MutableStateFlow<Response<Pair<Float, Float>>>(Response.Loading)
    val coordinates: StateFlow<Response<Pair<Float, Float>>> = _coordinates


    private val _windSpeedUnit = MutableStateFlow<Response<String>>(Response.Loading)
    val windSpeedUnit: StateFlow<Response<String>> = _windSpeedUnit


    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {

                 _temperatureUnit.value = Response.Success(
                        repository.getTemperatureUnit()
                        )

                _language.value = Response.Success(
                    repository.getLanguage()
                )

                _locationFinder.value = Response.Success(
                    repository.getLocationFinder()
                )

                _windSpeedUnit.value = Response.Success(
                    repository.getWindSpeedUnit()
                )

                // Load coordinates
                val lat = repository.getLatitude()
                val lon = repository.getLongitude()
                _coordinates.value = Response.Success(Pair(lat, lon))

            } catch (e: Exception) {
                _temperatureUnit.value = Response.Failure(e)
                _language.value = Response.Failure(e)
                _locationFinder.value = Response.Failure(e)
                _windSpeedUnit.value = Response.Failure(e)
                _coordinates.value = Response.Failure(e)
            }
        }
    }

    fun saveTemperatureUnit(system: String) {
        viewModelScope.launch {
            _temperatureUnit.value = Response.Loading
            try {
                repository.saveTemperatureUnit(system)
                _temperatureUnit.value = Response.Success(system)
            } catch (e: Exception) {
                _temperatureUnit.value = Response.Failure(e)
            }
        }
    }

    fun saveLanguage(lang: String) {
        viewModelScope.launch {
            _language.value = Response.Loading
            try {
                repository.saveLanguage(lang)
                _language.value = Response.Success(lang)
            } catch (e: Exception) {
                _language.value = Response.Failure(e)
            }
        }
    }

    fun saveLocationFinder(finder: String) {
        viewModelScope.launch {
            _locationFinder.value = Response.Loading
            try {
                repository.saveLocationFinder(finder)
                _locationFinder.value = Response.Success(finder)
            } catch (e: Exception) {
                _locationFinder.value = Response.Failure(e)
            }
        }
    }

    fun saveCoordinates(lat: Float, lon: Float) {
        viewModelScope.launch {
            _coordinates.value = Response.Loading
            try {
                repository.saveLatitude(lat)
                repository.saveLongitude(lon)
                _coordinates.value = Response.Success(Pair(lat, lon))
            } catch (e: Exception) {
                _coordinates.value = Response.Failure(e)
            }
        }
    }
}

class SettingsViewModelFactory(
    private val repository: SettingsRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}