package com.mariammuhammad.climate.map.view


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.favorite.viewmodel.FavoriteViewModel
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.home.viewmodel.WeatherFactory
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.model.pojo.Coord
import com.mariammuhammad.climate.utiles.Response

    @Composable
    fun MapScreen(favViewModel: FavoriteViewModel) {

        val next5Days =
            favViewModel.fiveDayFavoriteCity.collectAsStateWithLifecycle().value

        val searchPlaceCoordinates =
            favViewModel.searchPlaceCoordinates.collectAsStateWithLifecycle().value

        var searchText by remember { mutableStateOf("") }
        var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }

        val context = LocalContext.current.applicationContext

        val scope = rememberCoroutineScope()


        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyDjfrDmJIFJRxD4WGeq40osxSoGp6PrD4Y")
        }

        val places = remember { Places.createClient(context) }

        favViewModel.getLocationOnMap(searchText, places)

        Box(modifier = Modifier.fillMaxSize()) {
            LocationPickerMap(
                selectedLocation = selectedLatLng,
                onLocationSelected = { latLng ->
                    selectedLatLng = latLng
                }
            )


            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = {

                    if (searchPlaceCoordinates is Response.Success) {
                        selectedLatLng = searchPlaceCoordinates.data
                    }

                }),
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(top = 50.dp)
                    .padding(16.dp),
                label = { Text("Search for a place...") },
                singleLine = true
            )

            selectedLatLng?.let { latLng ->

                Card(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 100.dp)
                     .align(Alignment.BottomCenter),
                   colors = CardDefaults.cardColors(containerColor = Color(0xFF825F9D)),
 //                   colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))

                ) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    )
                    {

                        //check
                        Button(
                            onClick = {
                                favViewModel.getRemote5Days3HoursWeather(
                                    latLng.latitude,
                                    latLng.longitude,
                                    "metric",
                                    "en"
                                )
                                if (next5Days is Response.Success) {

                                    next5Days.data.city?.let {
                                        favViewModel.addFavoriteCity(next5Days.data.city)

                                    }


                                }
                            }
                        ) {

                            Icon(Icons.Outlined.Favorite, contentDescription = "Add to favorite")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                stringResource(R.string.save_location),
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LocationPickerMap(
        selectedLocation: LatLng?,
        onLocationSelected: (LatLng) -> Unit
    ) {

        val defaultLocation = remember { LatLng(30.0381736, 30.9793528) }

        var currentLocation = selectedLocation ?: defaultLocation

        var cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
        }

        LaunchedEffect(currentLocation) {
            selectedLocation?.let {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(currentLocation, 10f)
                )
            }
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                onLocationSelected(latLng)
            }
        ) {

            selectedLocation?.let { location ->
                Marker(
                    state = MarkerState(position = location),
                   // title = "Weather Location"
                )
            }
        }
    }


