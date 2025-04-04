package com.mariammuhammad.climate.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


@Serializable
sealed class NavigationRoute() {

    @Serializable
    object SplashScreen : NavigationRoute()
    @Serializable
    data class HomeScreen(val favLat:Double?=null, val favLon:Double?=null)  : NavigationRoute()

    @Serializable
    object WeatherAlertScreen : NavigationRoute()

    @Serializable
     object FavoriteScreen : NavigationRoute()

    @Serializable
    object MapScreen : NavigationRoute()

    @Serializable
    object SettingsScreen : NavigationRoute()
}




