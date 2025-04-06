package com.mariammuhammad.climate.settings.data

import android.content.SharedPreferences

interface ISettingsPrefs {
    val settingsPrefs: SharedPreferences
    fun getTemperatureUnit(): String
    fun saveTemperatureUnit(measurementSystemValue: String)

    //language settings
fun getLanguage(): String
    fun saveLanguage(lang: String)

    //location settings
 fun getLocationFinder(): String
    fun saveLocationFinder(locationFinder: String)

    //saved long
fun getLongitude(): Float
    fun saveLongitude(long: Float)

    //saved lat
fun getLatitude(): Float
    fun saveLatitude(lat: Float)

     fun getWindSpeedUnit(): String

    fun saveWindSpeedUnit(unit: String)
}