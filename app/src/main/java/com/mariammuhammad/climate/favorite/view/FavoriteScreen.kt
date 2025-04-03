package com.mariammuhammad.climate.favorite.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.favorite.viewmodel.FavoriteViewModel
import com.mariammuhammad.climate.map.view.MapScreen
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.navigation.NavigationRoute
import com.mariammuhammad.climate.utiles.Response
import com.mariammuhammad.climate.utiles.TimeAndDateFormatting.getCountryName

//class FavoriteScreen(favoriteViewModel: FavoriteViewModel) {

//    @Composable
//    fun FavoriteScreen(
//        favViewModel: FavoriteViewModel
//    ) {
//        val favCities by favViewModel.favoriteCities.collectAsStateWithLifecycle()
//        val snackBarHostState = remember { SnackbarHostState() }
//
//        Scaffold(
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = { navController.navigate("map_screen") }
//                ) {
//                    Icon(Icons.Default.Add, contentDescription = "Add location")
//                }
//            }
//        ) { padding ->
//            Column(
//                modifier = Modifier
//                    .padding(padding)
//                    .fillMaxSize()
//            ) {
//                when (favCities) {
//                    is Response.Loading -> {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    }
//
//                    is Response.Success -> {
//                        val cities = (favCities as Response.Success).data
//                        if (cities.isEmpty()) {
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text("No favorite locations yet")
//                            }
//                        } else {
//                            LazyColumn {
//                                items(cities) { city ->
//                                    FavoriteCityItem(
//                                        city = city,
//                                        onDelete = { favViewModel.removeFavoriteCity(city) },
//                                        onClick = {
//                                            // Navigate to forecast screen
//                                            navController.navigate("forecast/${city.coord.lat}/${city.coord.lon}")
//                                        }
//                                    )
//                                }
//                            }
//                        }
//                    }
//
//                    is Response.Failure -> {
//                        val error = (favCities as Response.Failure).error
//                        LaunchedEffect(snackBarHostState) {
//                            snackBarHostState.showSnackbar(
//                                message = "Error: ${error.localizedMessage}",
//                                duration = SnackbarDuration.Long
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun FavoriteCityItem(
//        city: City,
//        onDelete: () -> Unit,
//        onClick: () -> Unit
//    ) {
//        Card(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth()
//                .clickable { onClick() },
//            elevation = CardDefaults.cardElevation(4.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = city.name,
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                    Text(
//                        text = "Lat: ${city.coord.lat}, Lon: ${city.coord.lon}",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete")
//                }
//            }
//        }
//    }

@Composable
fun FavoriteScreen(favViewModel: FavoriteViewModel) {
    // Get NavController for navigation
    val navController = rememberNavController()

    favViewModel.getLocalFavCities()
    val favCities = favViewModel.favoriteCities.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navigate to MapScreen when FAB is clicked
                    //navController.navigate(NavigationRoute.MapScreen::class.java.simpleName)
//                    MapScreen(favViewModel)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add location")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colorResource(R.color.background)))
                {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        when {
                            favCities is Response.Success -> {
                                LazyColumn {
                                    items(favCities.data.size) { index ->
                                        FavCities(favCities.data[index], favViewModel, navController)
                                    }
                                }
                            }

                            favCities is Response.Loading -> {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                                }
                            }
                        }
                    }
                }
    }
}
//fun FavoriteScreen(favViewModel: FavoriteViewModel) {
//    // Get NavController for navigation
//    val navController = rememberNavController()
//
//    favViewModel.getLocalFavCities()
//    val favCities = favViewModel.favoriteCities.collectAsStateWithLifecycle().value
//    val snackBarHostState = remember { SnackbarHostState() }
//
//    // Show a snackbar when the cities are loaded successfully
//    LaunchedEffect(favCities) {
//        if (favCities is Response.Success) {
//            snackBarHostState.showSnackbar(
//                message = favCities.data.toString(),
//                duration = SnackbarDuration.Short
//            )
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(colorResource(R.color.background))
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//        ) {
//            when {
//                favCities is Response.Success -> {
//                    LazyColumn {
//                        items(favCities.data.size) { index ->
//                            // Pass the city to FavCities composable
//                            FavCities(favCities.data[index], favViewModel, navController)
//                        }
//                    }
//                }
//
//                favCities is Response.Loading -> {
//                    Row(
//                        modifier = Modifier.fillMaxSize(),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        CircularProgressIndicator(modifier = Modifier.size(100.dp))
//                    }
//                }
//            }
//        }
//
//        // Floating Action Button (FAB)
//        FloatingActionButton(
//            onClick = {
//                // When the FAB is clicked, navigate to the MapScreen
//                navController.navigate(NavigationRoute.MapScreen)
//            },
//            modifier = Modifier
//                .align(Alignment.BottomEnd) // Position the FAB at the bottom end
//                .padding(16.dp) // Add padding to the FAB
//        ) {
//            Icon(
//                imageVector = Icons.Default.AddCircle, // Use plus icon for the FAB
//                contentDescription = "Add location"
//            )
//        }
//    }
//}
    @Composable
    fun FavCities(city: City, favViewModel: FavoriteViewModel, navController: NavController) {

        val countryName = getCountryName(city.country)
        val lat = city.coord.lat.toString()
        val lon = city.coord.lon.toString()

        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(NavigationRoute.HomeFav(lat, lon))
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {

                Icon(imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        favViewModel.removeFavoriteCity(city)
                    })

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = countryName,
                    color = Color.White,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.width(80.dp))

                Text(
                    text = city.name,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )

            }
        }
    }





