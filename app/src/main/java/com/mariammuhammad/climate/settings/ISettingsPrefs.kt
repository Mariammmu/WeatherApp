package com.mariammuhammad.climate.settings

import android.content.SharedPreferences

interface ISettingsPrefs {

        val settingsPrefs: SharedPreferences
        fun getMeasurementSystem(): String
        fun saveMeasurementSystem(measurementSystemValue: String)
        fun saveLanguage(lang: String)

        fun getLocationFinder(): String
        fun saveLocationFinder(locationFinder: String)
    }
