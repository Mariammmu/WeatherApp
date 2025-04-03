package com.mariammuhammad.climate.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


@Serializable
sealed class NavigationRoute() {
    @Serializable
    object HomeScreen : NavigationRoute()

    @Serializable
    data class HomeFav (val favLat:String ="", val favLon:String = "") : NavigationRoute()
    @Serializable
    object WeatherAlertScreen : NavigationRoute()

    @Serializable
     object FavoriteScreen : NavigationRoute()

    @Serializable
    object MapScreen : NavigationRoute()

    @Serializable
    object SettingScreen : NavigationRoute()
}




