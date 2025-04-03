package com.mariammuhammad.climate.settings

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

class Settings {

    class WeatherSettings private constructor(context: Context) {

        companion object {
            const val PREFS_NAME = "weather_settings"
            const val TEMP_KEY = "temp_unit"
            const val LANG_KEY = "lang"
            const val WIND_KEY = "wind_unit"
            const val LOCATION_KEY = "location"
            const val SETTINGS_LATITUDE = "settings_latitude"
            const val SETTINGS_LONGITUDE = "settings_longitude"

            @Volatile
            private var INSTANCE: WeatherSettings? = null

            fun getInstance(context: Context): WeatherSettings {
                return INSTANCE ?: synchronized(this) {
                    val instance = WeatherSettings(context)
                    INSTANCE = instance
                    instance
                }
            }
        }

        private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        fun setTempUnit(tempUnit: String) = sharedPrefs.edit() { putString(TEMP_KEY, tempUnit) }

        fun getTempUnit() = sharedPrefs.getString(TEMP_KEY, "c")

        fun setLanguage(lang: String) = sharedPrefs.edit { putString(LANG_KEY, lang) }

        fun rememberLanguage(context: Context) {
            val locale = Locale(getLanguage() ?: "en")
            Locale.setDefault(locale)

            val resources = context.resources
            val config = Configuration(resources.configuration)
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            context.createConfigurationContext(config)
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        fun getLanguage() = sharedPrefs.getString(LANG_KEY, "en")

        fun setWindSpeedUnit(windSpeedUnit: String) =
            sharedPrefs.edit() { putString(WIND_KEY, windSpeedUnit) }

        fun getWindSpeedUnit() = sharedPrefs.getString(WIND_KEY, "m/s")

        fun setLocationPref(locationPref: String) =
            sharedPrefs.edit { putString(LOCATION_KEY, locationPref) }

        fun getLocationPref() = sharedPrefs.getString(LOCATION_KEY, "gps")

        fun setSettingLatitude(settingsLatitude: String) {
            sharedPrefs.edit() { putString(SETTINGS_LATITUDE, settingsLatitude) }
        }

        fun setSettingsLongitude(settingsLongitude: String) {
            sharedPrefs.edit() { putString(SETTINGS_LONGITUDE, settingsLongitude) }
        }

        fun getSettingLatitude(): String {
            return sharedPrefs.getString(SETTINGS_LATITUDE, "0").toString()
        }

        fun getSettingsLongitude(): String {
            return sharedPrefs.getString(SETTINGS_LONGITUDE, "0").toString()
        }


    }
}