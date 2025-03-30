package com.mariammuhammad.climate.home.view

import android.app.AlertDialog
import android.content.Context
import android.location.LocationManager
import android.telecom.Call.Details
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import android.Manifest
import androidx.annotation.Size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.ListItem
import com.mariammuhammad.climate.navigation.BottomNavigationBar
import com.mariammuhammad.climate.utiles.Constants
import com.mariammuhammad.climate.utiles.ImageIcon
import com.mariammuhammad.climate.utiles.LocationPermissionManager
import com.mariammuhammad.climate.utiles.LocationUpdate
import com.mariammuhammad.climate.utiles.Response
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting.dateTimeFormater
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting.timeFormater
import kotlinx.coroutines.coroutineScope
import java.util.Calendar
import java.util.Date

//@Preview(showSystemUi = true)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) { //


    var context = LocalContext.current
    val currentWeather by homeViewModel.currentWeather.collectAsState()
    val nextDaysWeather by homeViewModel.nextDaysWeather.collectAsState()
    val locationUpdate = LocationUpdate(context)
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var getData by rememberSaveable { mutableStateOf(true) }
    var isDataShown by rememberSaveable { mutableStateOf(false) }
    var unit by rememberSaveable { mutableStateOf("metric") }
    var lang by rememberSaveable { mutableStateOf("en") }

    val launcherActivity =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, check if location services are enabled
                if (locationUpdate.isLocationEnabled()) {
                    locationUpdate.getLastLocation { location ->
                        // Fetch weather data after getting the location
                        location?.let {
                            homeViewModel.getCurrentWeather(
                                it.latitude,
                                location.longitude,
                                units = unit,
                                lang = lang
                            )
                        }
                    }
                } else {
                    // Prompt user to enable location services
                    locationUpdate.promptEnableLocationSettings()
                }
            } else {
                // Show message if permission is denied
                Toast.makeText(context, "Please enable location permission.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    // Initialize LocationPermissionManager to request permission
    val locationPermissionManager = LocationPermissionManager(
        context
    )


    // Check if location permission is granted and location services are enabled
    LaunchedEffect(Unit) {  //non composable code inside a composable funstion
        if (locationPermissionManager.isLocationPermissionGranted()) {
            if (locationUpdate.isLocationEnabled()) {
                locationUpdate.getLastLocation { location ->
                    location?.let {
                        homeViewModel.getCurrentWeather(
                            it.latitude,
                            it.longitude,
                            units = unit,
                            lang = lang
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

    when (currentWeather) {
        is Response.Failure -> {
            isLoading = false
            if (!isDataShown) {
                val errorMsg = (currentWeather as Response.Failure).error
                ShowErrorMessage(errorMsg)
            }
        }

        Response.Loading -> {
        }


        is Response.Success -> {

            val currentWeatherDetails = (currentWeather as Response.Success<CurrentWeather>).data

            val hourlyForecast = when (nextDaysWeather) {
                is Response.Success ->
                    (nextDaysWeather as Response.Success<List<ListItem>>).data.take(9)

                else -> emptyList()
                //what does that mean
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
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
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

                            //Weather icon
                            GlideImage(
                                model = ImageIcon.getWeatherImage(
                                    "${currentWeatherDetails.weather?.first()?.icon}"
                                ),
                                //icon
                                contentDescription = "Weather Icon",
                                modifier = Modifier
                                    .size(160.dp)
                                    .padding(top = 4.dp),
                                contentScale = ContentScale.Fit
                            )
                            //val date = Date(currentWeatherDetails.dt)

                            //val calendar = Calendar.getInstance()
                            //calendar.time = date
                            //date and time
                            Text(
                                text = currentWeatherDetails.dt.dateTimeFormater(),   //calendar.get(Calendar.DAY_OF_WEEK).toString(),//currentWeatherDetails.dt.toString(),
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
                                    text = "${currentWeatherDetails.weatherDetails.temp.toInt()}",  //+"°C",
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
                                    text = "H${currentWeatherDetails.weatherDetails.tempMax.toInt()} ",
                                    fontSize = 18.sp,
                                    color = colorResource(R.color.off_white)
                                )
                                Text(
                                    text = "| L${currentWeatherDetails.weatherDetails.tempMin.toInt()}",
                                    fontSize = 18.sp,
                                    color = colorResource(R.color.off_white)
                                )
                            }

                            // Location
                            Text(
                                text = "${currentWeatherDetails.cityName}, ${currentWeatherDetails.country}",
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
                            modifier = Modifier.fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Text(
                                text = "Weather Details",
                                fontSize = 20.sp,
                                color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    // .fillMaxSize()
                                    // .padding(horizontal = 8.dp, vertical = 8.dp),
                                    .padding(bottom = 8.dp)
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
//                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                                        verticalAlignment = Alignment.CenterVertically
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    WeatherDetailItem(currentWeatherDetails)


                                }
                            }

                        }
                    }

                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {

                            Text(
                                text = "Today",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )

                            if (hourlyForecast.isNotEmpty()) {
                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(hourlyForecast) { item ->
                                        HourlyWeatherItem(item)
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
fun WeatherDetailItem(weatherDetails: CurrentWeather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // First Row - Pressure, Clouds, Wind Speed
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(
                label = stringResource(R.string.pressure),
                imageResource = R.drawable.pressure2,
                value = "${weatherDetails.weatherDetails.pressure} hpa"
            )

            WeatherDetailItem(
                label = stringResource(R.string.humidity),
                imageResource = R.drawable.humd,
                value = "${weatherDetails.weatherDetails.humidity}%"
            )

            WeatherDetailItem(
                label = stringResource(R.string.clouds),
                imageResource = R.drawable.clouds,
                value = "${weatherDetails.clouds.all}%"
            )



        }

        // Second Row - Humidity, Ultraviolet
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            WeatherDetailItem(
                label = stringResource(R.string.ultraviolet),
                imageResource = R.drawable.ultraviolet,
                value = "${weatherDetails.weatherDetails.tempKf}"
            )

            WeatherDetailItem(
                label = stringResource(R.string.wind_speed),
                imageResource = R.drawable.wind2,
                value = "${weatherDetails.wind.speed} m/s"
            )
        }
    }
}
@Composable
fun WeatherDetailItem(label: String, imageResource: Int, value: String) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(0.9f),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.Transparent.copy(alpha = 0.3f)
//        )
//    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Label
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(id = R.color.off_white),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Image
            Image(
                painter = painterResource(imageResource),
                contentDescription = label,
                modifier = Modifier
                    .size(60.dp)
                    .padding(vertical = 8.dp)
            )

            // Value
            Text(
                text = value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = colorResource(id = R.color.off_white),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
   // }
}

@Composable
fun HourlyWeatherForecast(hourlyDetails: List<ListItem>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Hourly Forecast",
            fontSize = 20.sp,
            color = colorResource(R.color.off_white),
            fontFamily = FontFamily(Font(R.font.alfa_slab)),
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hourlyDetails) { hourlyItem ->
                HourlyWeatherItem(item = hourlyItem)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyWeatherItem(item: ListItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(60.dp)
    ) {
        Text(
            text = item.dt.timeFormater(),
            fontSize = 14.sp,
            color = Color.White
        )
        GlideImage(
            model = ImageIcon.getWeatherImage(item.weather.first().icon),
            contentDescription = "Weather icon",
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


@Composable
fun ShowErrorMessage(errorMsg: Throwable) {
    Text(text = errorMsg.toString(), fontSize = 24.sp)
}

