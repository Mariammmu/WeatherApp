package com.mariammuhammad.climate.home.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import android.Manifest
import android.util.Log
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.ListDaysDetails
import com.mariammuhammad.climate.model.data.NextDaysWeather
//import com.mariammuhammad.climate.navigation.BottomNavigationBar
import com.mariammuhammad.climate.utiles.Constants
import com.mariammuhammad.climate.utiles.ImageIcon
import com.mariammuhammad.climate.utiles.LocationPermissionManager
import com.mariammuhammad.climate.utiles.LocationUpdate
import com.mariammuhammad.climate.utiles.Response
import com.mariammuhammad.climate.utiles.HelperFunctions.dateTimeFormater
import com.mariammuhammad.climate.utiles.HelperFunctions.dayFormater
import com.mariammuhammad.climate.utiles.HelperFunctions.getCountryName
import com.mariammuhammad.climate.utiles.HelperFunctions.timeFormater


//AIzaSyA5rMOFNsoUcLMTq3YiLny0A0mG48lmO3c
//Use this: AIzaSyDjfrDmJIFJRxD4WGeq40osxSoGp6PrD4Y
//AIzaSyDjfrDmJIFJRxD4WGeq40osxSoGp6PrD4Y
//@Preview(showSystemUi = true)

private var windSpeedUnit = ""

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel,favLat: Double?=null, favLon: Double?=null) {

    var context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
//    val weatherPrefs = remember { Settings.WeatherSettings.getInstance(context) }
    val message by homeViewModel.showMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentWeather by homeViewModel.currentWeather.collectAsState()
    val nextDaysWeather by homeViewModel.nextDaysWeather.collectAsState()
    val locationUpdate = LocationUpdate(context)
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isDataShown by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        homeViewModel.initialize(context)
    }

    LaunchedEffect(message) {
        message?.let {
            snackBarHostState.showSnackbar(it)
            homeViewModel.messageShown()
        }
    }

    val locationPermissionManager = LocationPermissionManager(context)


     val launcherActivity =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                if (locationUpdate.isLocationEnabled()) {
                    locationUpdate.getLastLocation { location ->
                        // fetch weather data after getting the location
                        location?.let {
                            homeViewModel.get5DaysWeather(
                                it.latitude,
                                location.longitude,
                                tempUnit = Constants.UNITS_CELSIUS,
                                lang = Constants.LANGUAGE_EN
                            )
                            homeViewModel.getCurrentWeather(
                                it.latitude,
                                location.longitude,
                                units = Constants.UNITS_CELSIUS,
                                lang = Constants.LANGUAGE_EN
                            )
                        }
                    }
                } else {
                    locationUpdate.promptEnableLocationSettings()
                }
            } else {
                // Show message if permission is denied
                locationPermissionManager.showLocationServiceRationaleDialog(context)
               // Toast.makeText(context, "Please enable location permission.", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(Unit) {  //non composable code inside a composable funstion
     if((favLat!=null && favLon!=null)){
         Log.i("TAG", "HomeScreen: if Fav  true")
         homeViewModel.getCurrentWeather(favLat,
             favLon,
             units = Constants.UNITS_CELSIUS,
             lang= Constants.LANGUAGE_EN
         )

         homeViewModel.get5DaysWeather(favLat,
             favLon,
             tempUnit = Constants.UNITS_CELSIUS,
             lang=Constants.LANGUAGE_EN
         )
     }else{
         Log.i("TAG", "HomeScreen: if Fav  false")

         if (locationPermissionManager.isLocationPermissionGranted()) {
             if (locationUpdate.isLocationEnabled()) {
                 locationUpdate.getLastLocation { location ->
                     location?.let {
                         homeViewModel.getCurrentWeather(
                             it.latitude,
                             it.longitude,
                             units = Constants.UNITS_CELSIUS,
                             lang = Constants.LANGUAGE_EN
                         )
                         homeViewModel.get5DaysWeather(
                             it.latitude,
                             location.longitude,
                             tempUnit = Constants.UNITS_CELSIUS,
                             lang = Constants.LANGUAGE_EN
                         )
                     }
                 }
             } else {
                 locationUpdate.promptEnableLocationSettings()
             }
         } else {
             launcherActivity.launch(Manifest.permission.ACCESS_FINE_LOCATION)
         }
     }
 }

    when (currentWeather) { //child
      is Response.Failure -> {
            isLoading = false
            if (!isDataShown) {
                val errorMsg = (currentWeather as Response.Failure).error
                ShowErrorMessage(errorMsg)
            }
        }

        Response.Loading -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }
        }

        is Response.Success -> {
            val currentWeatherDetails = (currentWeather as Response.Success<CurrentWeather>).data
            Log.i("TAG", "CurrentWeather:${currentWeatherDetails.name}")

            val hourlyForecast = when (nextDaysWeather) {
                is Response.Success -> {
                    val forecast = (nextDaysWeather as Response.Success<NextDaysWeather>).data
                    Log.i("HOURLY_DEBUG", "Forecast items: ${forecast.list?.size}")
                    forecast.list.take(12)
                }
                else -> emptyList()
            }

            val dailyForecast = when (nextDaysWeather) {
                is Response.Success -> {
                    val forecast = (nextDaysWeather as Response.Success<NextDaysWeather>).data
                    forecast.list.groupBy { it.dt.dayFormater() }
                        .values.map { it.first() }.take(5) ?: emptyList()
                }

                else -> emptyList()
            }


            Box(
                modifier = Modifier.fillMaxSize()
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.weather), // use a pic from drawable resource
                    contentDescription = "background image",
                    modifier = Modifier.fillMaxSize(), // to ensure the image fills the entire Box
                    contentScale = ContentScale.FillBounds
                )

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp) //3ashan el bottom nav bar
                ) {

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currentWeatherDetails.weather.first().description
                                    .replaceFirstChar { it.titlecase() },    //question

                                fontSize = 30.sp,
                                color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 24.dp),
                                textAlign = TextAlign.Center
                            )

                            GlideImage(
                                model = ImageIcon.getWeatherImage(
                                    currentWeatherDetails.weather.first().icon
                                ),
                                //icon
                                contentDescription = "Weather Icon",
                                modifier = Modifier
                                    .size(160.dp)
                                    .padding(top = 4.dp),
                                contentScale = ContentScale.Fit
                            )

                            Text(
                                text = currentWeatherDetails.dt.toLong()
                                    .dateTimeFormater(),   //calendar.get(Calendar.DAY_OF_WEEK).toString(),//currentWeatherDetails.dt.toString(),
                                fontSize = 14.sp, fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                // modifier = Modifier.padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                //weather temperature
                                Text(
                                    text = "${currentWeatherDetails.main.temp.toInt()}",
                                    fontSize = 60.sp, fontWeight = FontWeight.Bold,
                                    color = colorResource(R.color.off_white),
                                    fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                    modifier = Modifier
                                        .padding(top = 16.dp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = " °C",
                                    fontSize = 60.sp, fontWeight = FontWeight.Bold,
                                    color = colorResource(R.color.off_white),
                                    fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                    modifier = Modifier
                                        .padding(top = 16.dp),
                                    textAlign = TextAlign.Center

                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "H${currentWeatherDetails.main.tempMax.toInt()} ",
                                    fontSize = 18.sp,
                                    color = colorResource(R.color.off_white)
                                )
                                Text(
                                    text = "| L${currentWeatherDetails.main.tempMin.toInt()}",
                                    fontSize = 18.sp,
                                    color = colorResource(R.color.off_white)
                                )
                            }

                            // location
                            Text(
                                text = "${currentWeatherDetails.name}, ${
                                    getCountryName(currentWeatherDetails.sys.country
                                    )
                                }",
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 24.dp),
                                textAlign = TextAlign.Center
                            )

                        }

                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.weather_details),
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp),
                                fontFamily = FontFamily(Font(R.font.alfa_slab))
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .shadow(elevation = 4.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    WeatherDetails(currentWeatherDetails)


                                }
                            }

                        }
                    }
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.hourly_details),
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(16.dp),
                                fontFamily = FontFamily(Font(R.font.alfa_slab))

                            )

                            when {
                                hourlyForecast.isNotEmpty() -> {
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(horizontal = 16.dp)
                                    ) {
                                        items(hourlyForecast) { item ->
                                            HourlyWeatherItem(item)
                                        }
                                    }
                                }

                                nextDaysWeather is Response.Loading -> {
                                    Text(
                                        text = "Loading hourly data...",
                                        color = Color.White,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }

                                nextDaysWeather is Response.Failure -> {
                                    Text(
                                        text = "Failed to load hourly data",
                                        color = Color.Red,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }

                            item {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = stringResource(R.string.next_5_days),
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(16.dp),
                                        fontFamily = FontFamily(Font(R.font.alfa_slab))
                                    )

                                    if (dailyForecast.isEmpty()) {
                                        if (nextDaysWeather is Response.Loading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .align(Alignment.CenterHorizontally)
                                                    .padding(16.dp),
                                                color = Color.White
                                            )
                                        } else {
                                            Text(
                                                text = "No forecast data available",
                                                color = Color.White,
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }
                                    } else {
                                        Column {
                                            dailyForecast.forEach { day ->
                                                DailyForecastItem(day)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

@Composable
fun WeatherDetails(weatherDetails: CurrentWeather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(
                label = stringResource(R.string.pressure),
                imageResource = R.drawable.pressure2,
                value = "${weatherDetails.main.pressure} hpa"
            )

            WeatherDetailItem(
                label = stringResource(R.string.humidity),
                imageResource = R.drawable.humd,
                value = "${weatherDetails.main.humidity}%"
            )

            WeatherDetailItem(
                label = stringResource(R.string.clouds),
                imageResource = R.drawable.clouds,
                value = "${weatherDetails.clouds.all}%"
            )


        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            WeatherDetailItem(
                label = stringResource(R.string.ultraviolet),
                imageResource = R.drawable.ultraviolet,
                value = "${weatherDetails.main.feelsLike}"
            )

            WeatherDetailItem(
                label = stringResource(R.string.wind_speed),
                imageResource = R.drawable.wind2,
                value = "${weatherDetails.wind.speed} ${Constants.WIND_SPEED_MILE}" //
            )
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, imageResource: Int, value: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(id = R.color.off_white),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Image(
            painter = painterResource(imageResource),
            contentDescription = label,
            modifier = Modifier
                .size(60.dp)
                .padding(vertical = 8.dp)
        )

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = colorResource(id = R.color.off_white),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyWeatherItem(item: ListDaysDetails) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(90.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.dt.timeFormater(),
                fontSize = 12.sp,
                color = Color.White
            )
            GlideImage(
                model = ImageIcon.getWeatherImage(item.weather.first().icon),
                contentDescription = "Hourly weather",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "${item.main.temp.toInt()}°",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyForecastItem(day: ListDaysDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Text(
                    text = day.dt.dayFormater(),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.alfa_slab))
                )
            }

            GlideImage(
                model = ImageIcon.getWeatherImage(day.weather.first().icon),
                contentDescription = "Weather icon",
                modifier = Modifier.size(40.dp),

            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${day.main.tempMax.toInt()}° / ${day.main.tempMin.toInt()}°",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = "Feels like ${day.main.feelsLike.toInt()}°",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

// @Composable
//fun ErrorScreen(error: String, onRetry: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Error: $error",
//            color = Color.Red,
//            fontSize = 18.sp,
//            modifier = Modifier.padding(16.dp)
//        )
//        Button(onClick = onRetry) {
//            Text("Retry")
//        }
//    }
//}



@Composable
fun ShowErrorMessage(errorMsg: Throwable) {
    Text(text = errorMsg.toString(), fontSize = 24.sp)
}

