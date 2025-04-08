package com.mariammuhammad.climate.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ewida.skysense.util.NetworkStateObserver
import com.mariammuhammad.climate.home.view.HomeScreen
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.home.viewmodel.WeatherFactory
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.remote.RetrofitHelper.weatherService
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import com.mariammuhammad.climate.navigation.BottomNavigationBar
import com.mariammuhammad.climate.navigation.NavigationGraph
import com.mariammuhammad.climate.navigation.NavigationRoute
import com.mariammuhammad.climate.utiles.NetworkManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity(), NetworkStateObserver.NetworkCallbacksListener {  //AccessLocation

    var myLat:Double? = null
    var myLong:Double? = null

    lateinit var networkObserver: NetworkStateObserver

    //    lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        actionBar?.hide()


         networkObserver = NetworkStateObserver(this, networkCallbacksListener =this)

        val showSplashScreen = MutableStateFlow(true)
        installSplashScreen().setKeepOnScreenCondition {
            showSplashScreen.value
        }

//        val settingsPrefs = SettingsPrefs(context)
//        val languageHelper = LanguageHelper(settingsPrefs)

        lifecycleScope.launch{
            delay(3000)
            showSplashScreen.value =false
        }

        setContent {

            val navController= rememberNavController()
            NavigationGraph(navController)

        }
    }

    override fun onConnectionAvailable() {

        Toast.makeText(this,"Internet Connection Restored",Toast.LENGTH_LONG).show()
        }

    override fun onConnectionUnAvailable() {
        Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show()
    }

    //snackbar composable function should be inside a scaffold
    //snackbar Activity

    override fun onStart() {
        super.onStart()
        networkObserver.register()

    }
    override fun onPause() {
        super.onPause()
        networkObserver.unregister()

    }
    override fun onDestroy() {
        super.onDestroy()
        networkObserver.unregister()

    }
}






