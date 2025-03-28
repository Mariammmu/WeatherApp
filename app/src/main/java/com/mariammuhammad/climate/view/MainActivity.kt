package com.mariammuhammad.climate.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mariammuhammad.climate.home.view.HomeScreen
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.home.viewmodel.WeatherFactory
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.remote.RetrofitHelper.weatherService
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import com.mariammuhammad.climate.navigation.NavigationRoute


class MainActivity : ComponentActivity() {  //AccessLocation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


//viewModelProvider= viewModel() //save configuration changes
            var viewModel: HomeViewModel = viewModel(
                factory = WeatherFactory(
                    WeatherRepositoryImpl(
                        WeatherRemoteDataSource(weatherService)
                    )
                )
            )

             viewModel.getCurrentWeather(lat = 30.0444, lon = 31.2357, units = "metric",lang="en")

            HomeScreen(viewModel)


//            val navController = rememberNavController()
//// Step 2: Set up your NavHost with startDestination and composable destinations.
//            NavHost(
//                navController = navController,
//                startDestination = NavigationRoute.HomeScreen
//            ) {
//                composable<NavigationRoute.HomeScreen>() {
//                    // Display the SplashScreen composable when this route is navigated to.
//                   // HomeScreen(navController)
//                }
//            }

        }
    }
}










