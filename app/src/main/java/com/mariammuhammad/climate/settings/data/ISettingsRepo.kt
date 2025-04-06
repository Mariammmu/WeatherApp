package com.mariammuhammad.climate.settings.data

interface ISettingsRepo {
    fun getTemperatureUnit(): String
    fun saveTemperatureUnit(measurementSystem: String)
    fun getLanguage(): String
    fun saveLanguage(language: String)
    fun getLocationFinder(): String
    fun saveLocationFinder(locationFinder: String)
    fun getLongitude(): Float
    fun saveLongitude(longitude: Float)
    fun getLatitude(): Float
    fun saveLatitude(latitude: Float)
    fun getWindSpeedUnit(): String
    suspend fun saveWindSpeedUnit(unit: String)
}