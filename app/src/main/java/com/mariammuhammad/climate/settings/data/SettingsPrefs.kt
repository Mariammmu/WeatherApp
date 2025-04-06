package com.mariammuhammad.climate.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit



class SettingsPrefs (override val settingsPrefs: SharedPreferences) : ISettingsPrefs {

        private val tempUnit = "temp_unit"
        private val defaultUnitSystem = "default"

        private val language = "lang"
        private val defaultLanguage = "device_lang"

        private val locationFinder = "location"
        private val defaultLocation = "GPS"

        private val longitude = "longitude"
        private val defaultLongitude: Float = 0.0f

        private val latitude = "latitude"
        private val defaultLatitude: Float = 0.0f

    private val windSpeedUnitKey = "wind_speed_unit"
    private val defaultWindSpeedUnit = "m/s"

    override fun getWindSpeedUnit(): String {
        return settingsPrefs.getString(windSpeedUnitKey, defaultWindSpeedUnit) ?: defaultWindSpeedUnit
    }

    override fun saveWindSpeedUnit(unit: String) {
        settingsPrefs.edit {
            putString(windSpeedUnitKey, unit)
        }
}
         override fun getTemperatureUnit(): String {
            return settingsPrefs.getString(tempUnit, defaultUnitSystem)
                ?: defaultUnitSystem
        }

         override fun saveTemperatureUnit(measurementSystemValue: String) {
            settingsPrefs.edit() {
                putString(tempUnit, measurementSystemValue)
            }
        }


        override fun getLanguage(): String {
            return settingsPrefs.getString(language, defaultLanguage) ?: defaultLanguage
        }

         override fun saveLanguage(lang: String) {
            settingsPrefs.edit() {
                putString(language, lang)
            }
        }


        override fun getLocationFinder(): String {
            return settingsPrefs.getString(locationFinder, defaultLocation) ?: defaultLocation
        }

         override fun saveLocationFinder(locationFinder: String) {
            settingsPrefs.edit() {
                putString(this@SettingsPrefs.locationFinder, locationFinder)
            }
        }

        //saved long
        override fun getLongitude(): Float {
            return settingsPrefs.getFloat(longitude, defaultLongitude)
        }

        override fun saveLongitude(long: Float) {
            settingsPrefs.edit() {
                putFloat(longitude, long)
            }
        }

        override fun getLatitude(): Float {
            return settingsPrefs.getFloat(latitude, defaultLatitude)
        }

        override fun saveLatitude(lat: Float) {
            settingsPrefs.edit() {
                putFloat(latitude, lat)
            }
        }

    }
