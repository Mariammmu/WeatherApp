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
import androidx.compose.runtime.LaunchedEffect
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.ListItem
import com.mariammuhammad.climate.utiles.ImageIcon
import com.mariammuhammad.climate.utiles.LocationPermissionManager
import com.mariammuhammad.climate.utiles.LocationUpdate
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.coroutineScope

//private fun showRationaleDialog(context: Context, permissionManager: LocationPermissionManager) {
//    AlertDialog.Builder(context)
//        .setMessage("We need location permission to provide weather updates based on your location.")
//        .setPositiveButton("Grant Permission") { _, _ ->
//            permissionManager.requestLocationPermission()
//        }
//        .setNegativeButton("Cancel", null)
//        .show()
//}

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

//    var locationPermissionManager= LocationPermissionManager(context,
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),{
//            if(it){  //1 accept the permission //is granted
//                Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT).show()
//
//                locationUpdate.getCurrentLocation {
//                    homeViewModel.getCurrentWeather(it.latitude,it.longitude, units = "", lang="en")
//                }
//            }
//            else{
//                Toast.makeText(context, "Please enable the location.", Toast.LENGTH_SHORT).show()
//            }
//        }))

    // Initialize LocationPermissionManager to request permission
    val locationPermissionManager = LocationPermissionManager(
        context)



    // Check if location permission is granted and location services are enabled
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

//    val countryName = getCountryName(currentWeatherResponse?.sys?.country)
//
//    val sunrise = convertUnixTimeToTime(currentWeatherResponse?.sys?.sunrise?.toLong() ?: 0)
//    val sunset = convertUnixTimeToTime(currentWeatherResponse?.sys?.sunset?.toLong() ?: 0)
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
//            .background(brush = Brush.linearGradient(
//                colors = listOf(colorResource(R.color.background),
//                    colorResource(R.color.violet)
            )

            {
                Image(
                    painter = painterResource(id = R.drawable.weather), // use a pic from drawable resource
                    contentDescription = "background image",
                    modifier = Modifier.fillMaxSize(), // to ensure the image fills the entire Box
                    contentScale = ContentScale.FillBounds
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        item {
                            Text(
                                text =currentWeatherDetails.weather.first().description,  //description
                                fontSize = 24.sp,
                                color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 48.dp),
                                textAlign = TextAlign.Center
                            )

                            //Weather icon
                            GlideImage(
                                model =ImageIcon.
                                getWeatherImage("${currentWeatherDetails.weather?.first()?.icon}@2x.png"),
                                //icon
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp)
                                    .padding(top = 8.dp)
                            )

                            //Weather description
                            Text(
                                text =currentWeatherDetails.weather?.first()?.description.toString(),
                                fontSize = 19.sp,
                                color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 8.dp),
                                textAlign = TextAlign.Center
                            )

                            //weather temperature
                            Text(
                                text = currentWeatherDetails.weatherDetails.temp.toString(),
                                fontSize = 60.sp, fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 16.dp),
                                textAlign = TextAlign.Center
                            )


                            //date and time
                            Text(
                                text = currentWeatherDetails.dt.toString(),
                                fontSize = 16.sp, fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.off_white),
                                fontFamily = FontFamily(Font(R.font.alfa_slab)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 8.dp),
                                textAlign = TextAlign.Center
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 14.dp)
                                    .background(
                                        color = colorResource(R.color.background),
                                        shape = RoundedCornerShape(25.dp)
                                    )
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )
                                {
                                    
                                    WeatherDetailItem( currentWeatherDetails
                                    )


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
//means that each item in the row will be spaced by 4 dp from the next one.
//So, in short, the items in the LazyRow will have a gap of 4 dp between them horizontally.
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherDetailItem(weatherDetails: CurrentWeather) {

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            model = ImageIcon.getWeatherImage(weatherDetails.weather.first().icon),
            contentDescription = null,
            modifier = Modifier.size(34.dp)
        )

        Text(
            text = "${weatherDetails.wind.speed} m/s", fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.off_white),
            textAlign = TextAlign.Center
        )
    }
    Image(painter = painterResource(R.drawable.clouds),
        contentDescription = stringResource(R.string.clouds),
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    )
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.clouds), fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.off_white),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.alfa_slab)),

            )

        Text(text = "${weatherDetails.clouds.all}%",
            color= colorResource(R.color.off_white),
            fontFamily = FontFamily(Font(R.font.alfa_slab)),
        )
    }
}

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

