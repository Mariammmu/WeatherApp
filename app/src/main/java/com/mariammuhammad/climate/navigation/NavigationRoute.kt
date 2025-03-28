package com.mariammuhammad.climate.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute() {
    @Serializable
    object SplashScreen : NavigationRoute()

    @Serializable
    data object HomeScreen : NavigationRoute()

    @Serializable
    object WeatherAlertScreen : NavigationRoute()

    @Serializable
    data object FavoriteScreen : NavigationRoute()

    @Serializable
    object SettingScreen : NavigationRoute()
}
