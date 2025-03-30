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
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting.dateTimeFormater
import kotlinx.coroutines.coroutineScope
import java.util.Calendar
import java.util.Date

//@Preview(showSystemUi = true)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) { //


    var context= LocalContext.current
    val currentWeather by homeViewModel.currentWeather.collectAsState()
    val nextDaysWeather by homeViewModel.nextDaysWeather.collectAsState()
    val locationUpdate= LocationUpdate(context)
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var getData by rememberSaveable { mutableStateOf(true) }
    var isDataShown by rememberSaveable { mutableStateOf(false) }
    var unit by rememberSaveable { mutableStateOf("metric") }
    var lang by rememberSaveable { mutableStateOf("en") }

    val launcherActivity = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission granted, check if location services are enabled
            if (locationUpdate.isLocationEnabled()) {
                locationUpdate.getLastLocation { location ->
                    // Fetch weather data after getting the location
                    location?.let { homeViewModel.getCurrentWeather(it.latitude, location.longitude, units = unit, lang = lang) }
                }
            } else {
                // Prompt user to enable location services
                locationUpdate.promptEnableLocationSettings()
            }
        } else {
            // Show message if permission is denied
            Toast.makeText(context, "Please enable location permission.", Toast.LENGTH_SHORT).show()
        }
    }
    // Initialize LocationPermissionManager to request permission
    val locationPermissionManager = LocationPermissionManager(
        context)



    // Check if location permission is granted and location services are enabled
    LaunchedEffect(Unit) {  //non composable code inside a composable funstion
        if (locationPermissionManager.isLocationPermissionGranted()) {
            if (locationUpdate.isLocationEnabled()) {
                locationUpdate.getLastLocation { location ->
                    location?.let {
                        homeViewModel.getCurrentWeather(it.latitude, it.longitude, units = unit, lang = lang)
                    }
                }
            } else {
                locationUpdate.promptEnableLocationSettings()
            }
        } else {
            launcherActivity.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    //to give it lambda expression
    when(currentWeather){
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

           val currentWeatherDetails= (currentWeather as Response.Success<CurrentWeather>).data
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

                Column(modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 14.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        item {
                            Text(
                                text = currentWeatherDetails.weather.first().description,  //description like cloudy
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
                                model = ImageIcon.getWeatherImage("${currentWeatherDetails.weather?.first()?.icon}"),
                                //icon
                                contentDescription = "Weather Icon",
                                modifier = Modifier
                                    .size(180.dp)
                                    .padding(top = 4.dp)
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
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )
                            Row(
                                modifier = Modifier.padding(top = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Row {
                                    //weather temperature
                                    Text(
                                        text = "${currentWeatherDetails.weatherDetails.temp.toInt()} °C",  //+"°C",
                                        fontSize = 60.sp, fontWeight = FontWeight.Bold,
                                        color = colorResource(R.color.off_white),
                                        fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 16.dp),
                                        textAlign = TextAlign.Center
                                    )
//                                    Text(
//                                        text = Constants.UNITS_CELSIUS,
//                                        fontSize = 40.sp,
//                                        modifier = Modifier.padding(top = 16.dp)
//                                    )
                                }
                            }

                            //Question
                            Text(
                                text = "${currentWeatherDetails.cityName}, ${currentWeatherDetails.country}",
                                fontSize = 14.sp,
                                color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Weather Details",
                                fontSize = 20.sp, color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp, vertical = 14.dp),
                            )
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(horizontal = 14.dp, vertical = 14.dp)
//                                    .padding(top = 16.dp)
//                                    .background(
//                                        color = Color.Transparent,//colorResource(R.color.background),
//                                        shape = RoundedCornerShape(25.dp)
//                                    )


//                            Card(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(horizontal = 14.dp, vertical = 18.dp)
//                                    .shadow(elevation = 6.dp),
//                                shape = RoundedCornerShape(25.dp),
//                                colors = CardDefaults.cardColors(
//                                    containerColor = Color.Transparent
//                                )
//                            ) {
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(8.dp),
//                                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds space between each item
//                                ) {

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 18.dp)
                                    .shadow(elevation = 6.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        WeatherDetailItem(currentWeatherDetails)

                                    }
                                }
                            }


                            Text(
                                text = "Today",
                                fontSize = 20.sp, color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp, vertical = 8.dp),
                            )
                        }
                        item {
                            LazyRow(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            )
                            {

//                                items(items) { item ->
//                                    FutureModelViewHolder(item)
//                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Future", fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                    color = colorResource(R.color.off_white),
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = "Next 5 days", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                    color = colorResource(R.color.off_white)
                                )
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
    // Wrap all details inside a Column
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pressure
        WeatherDetailRow(
            label = stringResource(R.string.pressure),
            imageResource = R.drawable.pressure,
            value = "${weatherDetails.weatherDetails.pressure} hpa"
        )

        // Wind Speed
        WeatherDetailRow(
            label = stringResource(R.string.wind_speed),
            imageResource = R.drawable.wind2,
            value = "${weatherDetails.wind.speed} m/s"
        )

        // Clouds
        WeatherDetailRow(
            label = stringResource(R.string.clouds),
            imageResource = R.drawable.clouds,
            value = "${weatherDetails.clouds.all}%"
        )

        // Humidity
        WeatherDetailRow(
            label = stringResource(R.string.humidity),
            imageResource = R.drawable.humd,
            value = "${weatherDetails.weatherDetails.humidity}%"
        )

        WeatherDetailRow(
            label = stringResource(R.string.ultraviolet),
            imageResource = R.drawable.ultraviolet,
            value = "${weatherDetails.weatherDetails.tempKf}%"
        )
    }
}

//displaying each weather detail (Label, Image, Value)
@Composable
fun WeatherDetailRow(label: String, imageResource: Int, value: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Label
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.off_white),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.alfa_slab)),
        )

        // Image
        Image(
            painter = painterResource(imageResource),
            contentDescription = label,
            modifier = Modifier
                .size(40.dp)
                .padding(start = 5.dp, end = 5.dp)
        )

        // Value
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.off_white),
            textAlign = TextAlign.Center
        )
    }
}


//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun WeatherDetailItem(weatherDetails: CurrentWeather) {
//    Column(
//        modifier = Modifier.padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = stringResource(R.string.pressure), fontWeight = FontWeight.SemiBold,
//            color = colorResource(id = R.color.off_white),
//            textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.alfa_slab)),
//        )
//        Image(painter = painterResource(R.drawable.pressure),
//            contentDescription = stringResource(R.string.clouds),
//            modifier = Modifier.size(40.dp)
//                .padding(start = 5.dp, end = 5.dp)
//        )
//
//        Text(
//            text = "${weatherDetails.weatherDetails.pressure} hpa", fontWeight = FontWeight.Bold,
//            color = colorResource(id = R.color.off_white),
//            textAlign = TextAlign.Center
//        )
//    }
//
//    Column(
//        modifier = Modifier.padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = stringResource(R.string.humidity), fontWeight = FontWeight.SemiBold,
//            color = colorResource(id = R.color.off_white),
//            textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.alfa_slab)),
//        )
//        Image(painter = painterResource(R.drawable.humidity),
//            contentDescription = stringResource(R.string.clouds),
//            modifier = Modifier.size(40.dp)
//                .padding(start = 5.dp, end = 5.dp)
//        )
//
//        Text(
//            text = "${weatherDetails.weatherDetails.humidity}%", fontWeight = FontWeight.Bold,
//            color = colorResource(id = R.color.off_white),
//            textAlign = TextAlign.Center
//        )
//    }
//
//    Column(
//        modifier = Modifier.padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = stringResource(R.string.wind_speed), fontWeight = FontWeight.SemiBold,
//            color = colorResource(id = R.color.off_white),
//            textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.alfa_slab)),
//        )
//        Image(painter = painterResource(R.drawable.wind2),
//            contentDescription = stringResource(R.string.clouds),
//            modifier = Modifier.size(40.dp)
//                .padding(start = 5.dp, end = 5.dp)
//        )
//
//        Text(
//            text = "${weatherDetails.wind.speed} m/s", fontWeight = FontWeight.Bold,
//            color = colorResource(id = R.color.off_white),
//            textAlign = TextAlign.Center
//        )
//    }
//
//    Column(
//        modifier = Modifier.padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = stringResource(R.string.clouds), fontWeight = FontWeight.SemiBold,
//            color = colorResource(id = R.color.off_white),
//            textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.alfa_slab)),
//        )
//
//        Image(painter = painterResource(R.drawable.clouds),
//            contentDescription = stringResource(R.string.clouds),
//            modifier = Modifier.size(40.dp)
//            .padding(start = 5.dp, end = 5.dp)
//        )
//
//        Text(text = "${weatherDetails.clouds.all}%",
//            color= colorResource(R.color.off_white),
//            fontFamily = FontFamily(Font(R.font.alfa_slab)),
//        )
//    }
//}

@Composable
fun HourlyWeather(hourlyDetails: List<ListItem>) {
    //Box (modifier = ){  }
    Column(
        modifier = Modifier
            .width(90.dp)
            .wrapContentHeight()
            .padding(4.dp)
            .background(
                color = colorResource(R.color.background),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        LazyRow (horizontalArrangement = Arrangement.spacedBy(15.dp)){
            items(hourlyDetails.size){

            }
        }

    }
}


@Composable
private fun HourlyItem(details: ListItem){
    val SplittedDate= details.dtTxt.split(" ")
    val time= SplittedDate[1]
   // val formattedTime= ti

    Card (shape = RoundedCornerShape(16.dp), colors= CardDefaults.elevatedCardColors(
        containerColor = colorResource(R.color.background)),
        modifier = Modifier.width(180.dp).height(150.dp)

    ) {
        Row {

        }
    }
}

fun getHourlyWeather(lat: Double, lon: Double){

}
@Composable
fun ShowErrorMessage(errorMsg: Throwable) {
    Text(text = errorMsg.toString(), fontSize = 24.sp)
}

