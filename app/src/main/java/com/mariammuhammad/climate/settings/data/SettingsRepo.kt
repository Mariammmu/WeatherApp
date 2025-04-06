package com.mariammuhammad.climate.settings.data

class SettingsRepo(private val settingsPrefs: SettingsPrefs) : ISettingsRepo {
     override fun getTemperatureUnit(): String = settingsPrefs.getTemperatureUnit()

     override fun saveTemperatureUnit(measurementSystem: String) =
        settingsPrefs.saveTemperatureUnit(measurementSystem)

     override fun getLanguage(): String = settingsPrefs.getLanguage()

     override fun saveLanguage(language: String) = settingsPrefs.saveLanguage(language)

     override fun getLocationFinder(): String = settingsPrefs.getLocationFinder()

     override fun saveLocationFinder(locationFinder: String) =
        settingsPrefs.saveLocationFinder(locationFinder)

     override fun getLongitude(): Float = settingsPrefs.getLongitude()
     override fun saveLongitude(longitude: Float) = settingsPrefs.saveLongitude(longitude)

     override fun getLatitude(): Float = settingsPrefs.getLatitude()
     override fun saveLatitude(latitude: Float) = settingsPrefs.saveLatitude(latitude)

     override fun getWindSpeedUnit(): String {
          return settingsPrefs.getWindSpeedUnit() ?: "m/s" // Default to m/s
     }

     override suspend fun saveWindSpeedUnit(unit: String) {
          settingsPrefs.saveWindSpeedUnit(unit)
     }
}