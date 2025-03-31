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
import android.util.Log
import androidx.annotation.Size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.navigation.BottomNavigationBar
import com.mariammuhammad.climate.utiles.Constants
import com.mariammuhammad.climate.utiles.ImageIcon
import com.mariammuhammad.climate.utiles.LocationPermissionManager
import com.mariammuhammad.climate.utiles.LocationUpdate
import com.mariammuhammad.climate.utiles.Response
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting.dateTimeFormater
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting.dayFormater
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting.timeFormater
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
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
    val scrollState = rememberScrollState()


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

            //  val hourlyForecast = when (nextDaysWeather) {
//                is Response.Success -> {

            Log.i("HOURLY_DEBUG", "NextDaysWeather state: $nextDaysWeather")

            val hourlyForecast = when (nextDaysWeather) {
                is Response.Success -> {
                    val forecast = (nextDaysWeather as Response.Success<NextDaysWeather>).data
                    Log.i("HOURLY_DEBUG", "Forecast items: ${forecast.list?.size}")
                    forecast.list?.take(4) ?: emptyList()
                }

                else -> emptyList()
            }

            val dailyForecast = when (nextDaysWeather) {
                is Response.Success -> {
                    val forecast = (nextDaysWeather as Response.Success<NextDaysWeather>).data
                    // Group by day and take first item of each day
                    forecast.list?.groupBy { it.dt.toLong().dateTimeFormater() }
                        ?.values?.map { it.first() }?.take(5) ?: emptyList()
                }

                else -> emptyList()
            }


//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(scrollState)  // Make the column scrollable
//                    .padding(16.dp)
//            ) {
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
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


                            //Weather icon
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
                            //val date = Date(currentWeatherDetails.dt)

                            //val calendar = Calendar.getInstance()
                            //calendar.time = date
                            //date and time
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
                                    text = "${currentWeatherDetails.main.temp.toInt()}",  //+"°C",
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
                                    text = "H${currentWeatherDetails.main.temp_max.toInt()} ",
                                    fontSize = 18.sp,
                                    color = colorResource(R.color.off_white)
                                )
                                Text(
                                    text = "| L${currentWeatherDetails.main.temp_min.toInt()}",
                                    fontSize = 18.sp,
                                    color = colorResource(R.color.off_white)
                                )
                            }

                            // Location
                            Text(
                                text = "${currentWeatherDetails.name}, ${currentWeatherDetails.sys.country}",
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
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Weather Details",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
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
                                text = "Hourly Details",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
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
                                        text = "Next 5 Days",
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(16.dp)
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



//                    item {
//                        Column(modifier = Modifier.fillMaxWidth()) {
//                            Text(
//                                text = "Hourly Details",
//                                fontSize = 20.sp,
//                                color = Color.White,
//                                modifier = Modifier.padding(16.dp)
//                            )
//
//                            if (hourlyForecast.isEmpty()) {
//                                Text(
//                                    text = "Loading hourly data...",
//                                    color = Color.White,
//                                    modifier = Modifier.padding(16.dp)
//                                )
//                            } else {
//                                LazyRow(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    contentPadding = PaddingValues(horizontal = 16.dp),
//                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                                ) {
//                                    items(hourlyForecast) { item ->
//                                        HourlyWeatherItem(item)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//                               // } else {
//                                    Text(
//                                        text = "No hourly data available",
//                                        color = Color.White,
//                                        modifier = Modifier.padding(16.dp)
//                                    )
//                                }
                            //}


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
                value = "${weatherDetails.main.feels_like}"
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
    // }
}


// Keep your existing HourlyWeatherItem composable as is
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyWeatherItem(item: CurrentWeather) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.dt.toLong().timeFormater(),
                fontSize = 14.sp,
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
fun DailyForecastItem(day: CurrentWeather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Day of week
            Text(
                text = day.dt.toLong().dayFormater(),
                color = Color.White,
                fontSize = 16.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Weather icon
                GlideImage(
                    model = ImageIcon.getWeatherImage(day.weather.first().icon),
                    contentDescription = "Weather icon",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Temperatures
                Text(
                    text = "${day.main.temp_max.toInt()}° / ${day.main.temp_min.toInt()}°",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

//@Composable
//fun TodayHourlyData(hourlyData: List<CurrentWeather>) {
//    if (!hourlyData.isNullOrEmpty()) {
//        Text(
//            text = "Hourly Details",
//            style = MaterialTheme.typography.titleLarge,
//            textAlign = TextAlign.Start,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//        )
//        LazyRow(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
//            items(hourlyData.size) {
//                //                HourlyWeatherItem(hourlyData[it])
//                hourlyData[it]?.let { it1 -> HourlyWeatherItem(it1) }
//            }
//        }
//    }
//}

//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun HourlyWeatherItem(item: CurrentWeather) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .width(80.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
//    ) {
//        Column(
//            modifier = Modifier.padding(8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = item.dt.toLong().timeFormater(),
//                fontSize = 14.sp,
//                color = Color.White
//            )
//            GlideImage(
//                model = ImageIcon.getWeatherImage(item.weather.first().icon),
//                contentDescription = "Hourly weather",
//                modifier = Modifier.size(40.dp)
//            )
//            Text(
//                text = "${item.main.temp.toInt()}°",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            )
//        }
//    }
//}

/*
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
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.dt.timeFormater(),
                fontSize = 14.sp,
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
*/
@Composable
fun ShowErrorMessage(errorMsg: Throwable) {
    Text(text = errorMsg.toString(), fontSize = 24.sp)
}

