package com.mariammuhammad.climate.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mariammuhammad.climate.home.view.HomeScreen
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.home.viewmodel.WeatherFactory
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.remote.RetrofitHelper.weatherService
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.HomeScreen
    ) {
        composable<NavigationRoute.HomeScreen>() {
            //viewModelProvider= viewModel() //save configuration changes
            var viewModel: HomeViewModel = viewModel(
                factory = WeatherFactory(
                    WeatherRepositoryImpl(
                        WeatherRemoteDataSource(weatherService)
                    )
                )
            )
            HomeScreen(viewModel)
        }
        composable<NavigationRoute.WeatherAlertScreen>() {

        }
        composable<NavigationRoute.FavoriteScreen>() {
            //FavoriteScreen()
        }
        composable<NavigationRoute.SettingScreen>() {

        }
    }
}



