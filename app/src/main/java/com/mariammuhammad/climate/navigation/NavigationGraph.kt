package com.mariammuhammad.climate.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mariammuhammad.climate.favorite.view.FavoriteScreen
import com.mariammuhammad.climate.home.view.HomeScreen
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.home.viewmodel.WeatherFactory
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.remote.RetrofitHelper.weatherService
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationGraph(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.HomeScreen,
            modifier = Modifier.padding(paddingValues)

        ) {
            composable<NavigationRoute.HomeScreen>() {
                //viewModelProvider= viewModel() //save configuration changes
                val viewModel: HomeViewModel = viewModel(
                    factory = WeatherFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService)
                        )
                    )
                )
                // Scaffold(bottomBar = { BottomNavigationBar(navController) }) {
                HomeScreen(viewModel)

            }
            composable<NavigationRoute.WeatherAlertScreen>() {

            }
            composable<NavigationRoute.FavoriteScreen>() {
                FavoriteScreen()
            }
            composable<NavigationRoute.SettingScreen>() {

            }
        }
    }
}



