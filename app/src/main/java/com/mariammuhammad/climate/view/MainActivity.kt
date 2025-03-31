package com.mariammuhammad.climate.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mariammuhammad.climate.home.view.HomeScreen
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.home.viewmodel.WeatherFactory
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.remote.RetrofitHelper.weatherService
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import com.mariammuhammad.climate.navigation.BottomNavigationBar
import com.mariammuhammad.climate.navigation.NavigationGraph
import com.mariammuhammad.climate.navigation.NavigationRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {  //AccessLocation

    //    lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        actionBar?.hide()

        val showSplashScreen = MutableStateFlow(true)
        installSplashScreen().setKeepOnScreenCondition {
            showSplashScreen.value
        }

        // Initialize viewModel
//        val viewModel: HomeViewModel = viewModel(
//            factory = WeatherFactory(
//                WeatherRepositoryImpl(
//                    WeatherRemoteDataSource(weatherService)
//                )
//            )
//        )

        lifecycleScope.launch{

            delay(3000)
            showSplashScreen.value =false
        }

        setContent {

            val navController= rememberNavController()
            NavigationGraph(navController)

//viewModelProvider= viewModel() //save configuration changes
//            var viewModel: HomeViewModel = viewModel(
//                factory = WeatherFactory(
//                    WeatherRepositoryImpl(
//                        WeatherRemoteDataSource(weatherService)
//                    )
//                )
////            )
//            val navController = rememberNavController()
//            val startDestination = NavigationRoute.HomeScreen
//            NavHost(navController, startDestination) {
//                NavigationRoute.HomeScreen
//            }HomeScreen

//             viewModel.getCurrentWeather(lat = 30.0444, lon = 31.2357, units = "metric",lang="en")
          //  HomeScreen(viewModel)


//            val navController = rememberNavController()
//
//            // Navigation setup
//            NavHost(navController = navController, startDestination = HomeScreen(viewModel)) {
//                composable(NavigationRoute.HomeScreen) {
//                    HomeScreen(viewModel)
//                }
//               // composable(NavigationRoute.PermissionRequestScreen) {
//                    // Handle location permission UI here
//                }
//            }
//
//            // Simulate delay for splash screen
//            LaunchedEffect(Unit) {
//                delay(2000)  // 2 seconds delay
//                showSplashScreen.value = false
//            }
        }
    }
}






