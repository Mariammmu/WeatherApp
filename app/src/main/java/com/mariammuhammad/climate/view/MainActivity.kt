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


        lifecycleScope.launch{
            delay(3000)
            showSplashScreen.value =false
        }

        setContent {

            val navController= rememberNavController()
            NavigationGraph(navController)

        }
    }
}






