package com.mariammuhammad.climate.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mariammuhammad.climate.favorite.view.FavoriteScreen
import com.mariammuhammad.climate.favorite.viewmodel.FavoriteViewModel
import com.mariammuhammad.climate.favorite.viewmodel.FavoriteViewModelFactory
import com.mariammuhammad.climate.home.view.HomeScreen
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.home.viewmodel.WeatherFactory
import com.mariammuhammad.climate.map.view.MapScreen
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.local.WeatherDataBase
import com.mariammuhammad.climate.model.local.WeatherLocalDataSource
import com.mariammuhammad.climate.model.remote.RetrofitHelper.weatherService
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import com.mariammuhammad.climate.settings.view.SettingsScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationGraph(navController: NavHostController) {
    val context = LocalContext.current

    fun navigateToMapScreen() {
        navController.navigate(NavigationRoute.MapScreen::class.java.simpleName)
    }
    fun navigateToHomeScreen() {
        navController.navigate(NavigationRoute.HomeScreen::class.java.simpleName)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },

    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.HomeScreen::class.java.simpleName,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationRoute.HomeScreen::class.java.simpleName) {
                val viewModel: HomeViewModel = viewModel(
                    factory = WeatherFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(WeatherDataBase.getInstance(context).getFavoritesDao(),
                                WeatherDataBase.getInstance(context).getWeatherDao())
                        )
                    )
                )
                HomeScreen(viewModel)
            }

            composable(NavigationRoute.WeatherAlertScreen::class.java.simpleName) {
                // Implement the WeatherAlertScreen UI
            }

            composable(NavigationRoute.FavoriteScreen::class.java.simpleName) {
                val favoriteViewModel: FavoriteViewModel = viewModel(
                    factory = FavoriteViewModelFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(WeatherDataBase.getInstance(context).getFavoritesDao(),
                                WeatherDataBase.getInstance(context).getWeatherDao())
                        )
                    )
                )
                FavoriteScreen(favoriteViewModel,  onMapButtonClick = { navigateToMapScreen() } )
            }
            composable(NavigationRoute.MapScreen::class.java.simpleName) {
                val favoriteViewModel: FavoriteViewModel = viewModel(
                    factory = FavoriteViewModelFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(WeatherDataBase.getInstance(context).getFavoritesDao(),
                                WeatherDataBase.getInstance(context).getWeatherDao())
                        )
                    )
                )

                MapScreen(favoriteViewModel)
            }


            composable(NavigationRoute.SettingScreen::class.java.simpleName) {
                SettingsScreen()
            }
        }
    }
}
/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationGraph(navController: NavHostController) {
    val context = LocalContext.current

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.HomeScreen.toString(),
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<NavigationRoute.HomeScreen>() {
                val viewModel: HomeViewModel = viewModel(
                    factory = WeatherFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(WeatherDataBase.getInstance(context).getFavoritesDao())
                        )
                    )
                )
                HomeScreen(viewModel)
            }

            composable<NavigationRoute.WeatherAlertScreen>() {
                // Implement the WeatherAlertScreen UI
            }

            composable<NavigationRoute.FavoriteScreen>() {
                val favoriteViewModel: FavoriteViewModel = viewModel(
                    factory = FavoriteViewModelFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(WeatherDataBase.getInstance(context).getFavoritesDao())
                        )
                    )
                )
                FavoriteScreen(favoriteViewModel)
            }

            composable<NavigationRoute.SettingScreen>() {
                // Implement the SettingScreen UI
            }
        }
    }
}

 */