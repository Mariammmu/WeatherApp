package com.mariammuhammad.climate.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mariammuhammad.climate.Alert.view.WeatherAlarmScreen
import com.mariammuhammad.climate.Alert.viewmodel.AlertViewModel
import com.mariammuhammad.climate.Alert.viewmodel.AlertViewModelFactory
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
import com.mariammuhammad.climate.model.pojo.Alarm
import com.mariammuhammad.climate.model.remote.RetrofitHelper.weatherService
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import com.mariammuhammad.climate.settings.view.SettingsScreen
import com.mariammuhammad.climate.view.SplashScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationGraph(navController: NavHostController) {
    val context = LocalContext.current
    fun navigateToMapScreen() {
        navController.navigate(NavigationRoute.MapScreen)
    }

    fun navigateToHomeScreen(favLat: Double, favLon: Double) {
        navController.navigate(NavigationRoute.HomeScreen(favLat, favLon))
    }


    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },

        ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.SplashScreen,//NavigationRoute.HomeScreen(), //::class.java.simpleName,//"homeScreen/{favLat}/{favLon}",
            modifier = Modifier.padding(paddingValues)
        ) {

            composable<NavigationRoute.SplashScreen> { navBackStackEntry ->
                SplashScreen(
                    navigateToHome = {
                        navController.navigate(NavigationRoute.HomeScreen()) {
                            popUpTo<NavigationRoute.SplashScreen> {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<NavigationRoute.HomeScreen> { backStackEntry ->//("homeScreen/{favLat}/{favLon}") { backStackEntry ->
//                val favLat = backStackEntry.arguments?.getString("favLat")?.toDouble() ?: 0.0
//                val favLon = backStackEntry.arguments?.getString("favLon")?.toDouble() ?: 0.0
                val data = backStackEntry.toRoute<NavigationRoute.HomeScreen>()
                val viewModel: HomeViewModel = viewModel(
                    factory = WeatherFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(
                                WeatherDataBase.getInstance(context).getFavoritesDao(),
                                WeatherDataBase.getInstance(context).getWeatherDao(),
                                WeatherDataBase.getInstance(context).getAlarmDao()
                            )
                        )
                    )
                )
                HomeScreen(viewModel, favLat = data.favLat, favLon = data.favLon)
            }

            composable<NavigationRoute.FavoriteScreen> {
                val favoriteViewModel: FavoriteViewModel = viewModel(
                    factory = FavoriteViewModelFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(
                                WeatherDataBase.getInstance(context).getFavoritesDao(),
                                WeatherDataBase.getInstance(context).getWeatherDao(),
                                WeatherDataBase.getInstance(context).getAlarmDao()
                            )
                        )
                    )
                )
                FavoriteScreen(
                    favoriteViewModel,
                    onMapButtonClick = { navigateToMapScreen() },

                    onFavPlaceClick = { favLat, favLon ->
                        Log.i("TAG", "NavigationGraph: ${favLat}/${favLon}")
                        navigateToHomeScreen(favLat, favLon)
                    }

                )
            }
            composable<NavigationRoute.MapScreen> {
                val favoriteViewModel: FavoriteViewModel = viewModel(
                    factory = FavoriteViewModelFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(
                                WeatherDataBase.getInstance(context).getFavoritesDao(),
                                WeatherDataBase.getInstance(context).getWeatherDao(),
                                WeatherDataBase.getInstance(context).getAlarmDao()

                            )
                        )
                    )
                )

                MapScreen(favoriteViewModel)
            }


            composable<NavigationRoute.WeatherAlertScreen>() {

                val alertViewModel: AlertViewModel = viewModel(
                    factory = AlertViewModelFactory(
                        WeatherRepositoryImpl(
                            WeatherRemoteDataSource(weatherService),
                            WeatherLocalDataSource(
                                WeatherDataBase.getInstance(context).getFavoritesDao(),
                                WeatherDataBase.getInstance(context).getWeatherDao(),
                                WeatherDataBase.getInstance(context).getAlarmDao()
                            )
                        )
                    )
                )
                WeatherAlarmScreen(
                    alertViewModel,
                    //onNavigateToAlarmDetail=
                    )
            }

            composable<NavigationRoute.SettingsScreen> {
                SettingsScreen()
            }
        }
    }
}
