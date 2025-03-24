package com.mariammuhammad.climate.utiles

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute() {
    @Serializable
    object HomeScreen : NavigationRoute()

    @Serializable
    object WeatherAlertScreen : NavigationRoute()

    @Serializable
    data object FavoriteScreen : NavigationRoute()

    @Serializable
    object SettingScreen : NavigationRoute()
}
