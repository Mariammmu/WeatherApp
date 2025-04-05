package com.mariammuhammad.climate.settings

import android.content.SharedPreferences
import androidx.core.content.edit



class SettingsPrefs (override val settingsPrefs: SharedPreferences) : ISettingsPrefs {

        private val measurementSystem = "measurement_system"
        private val defaultUnitSystem = "default"

        private val language = "lang"
        private val defaultLanguage = "device_lang"

        private val locationFinder = "location"
        private val defaultLocation = "GPS"

        private val longitude = "longitude"
        private val defaultLongitude: Float = 0.0f

        private val latitude = "latitude"
        private val defaultLatitude: Float = 0.0f


        override fun getMeasurementSystem(): String {
            return settingsPrefs.getString(measurementSystem, defaultUnitSystem)
                ?: defaultUnitSystem
        }

        override fun saveMeasurementSystem(measurementSystemValue: String) {
            settingsPrefs.edit() {
                putString(measurementSystem, measurementSystemValue)
            }
        }


        //language settings
        fun getLanguage(): String {
            return settingsPrefs.getString(language, defaultLanguage) ?: defaultLanguage
        }

        override fun saveLanguage(lang: String) {
            settingsPrefs.edit() {
                putString(language, lang)
            }
        }


        //location settings
        override fun getLocationFinder(): String {
            return settingsPrefs.getString(locationFinder, defaultLocation) ?: defaultLocation
        }

        override fun saveLocationFinder(locationFinder: String) {
            settingsPrefs.edit() {
                putString(this@SettingsPrefs.locationFinder, locationFinder)
            }
        }

        //saved long
        fun getLongitude(): Float {
            return settingsPrefs.getFloat(longitude, defaultLongitude)
        }

        fun saveLongitude(long: Float) {
            settingsPrefs.edit() {
                putFloat(longitude, long)
            }
        }

        //saved lat
        fun getLatitude(): Float {
            return settingsPrefs.getFloat(latitude, defaultLatitude)
        }

        fun saveLatitude(lat: Float) {
            settingsPrefs.edit() {
                putFloat(latitude, lat)
            }
        }

    }
