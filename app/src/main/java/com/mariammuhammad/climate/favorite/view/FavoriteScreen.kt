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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.favorite.viewmodel.FavoriteViewModel
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.utiles.Response
import com.mariammuhammad.climate.utiles.HelperFunctions.getCountryName


@Composable
fun FavoriteScreen(favViewModel: FavoriteViewModel, onMapButtonClick: () -> Unit, onFavPlaceClick: (Double, Double) -> Unit) {

    favViewModel.getFavCities()
    val favCities = favViewModel.favoriteCities.collectAsStateWithLifecycle().value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick =
                      onMapButtonClick,

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
                                        FavCityItem(city = favCities.data[index],
                                            favViewModel = favViewModel) { lat, lon ->
                                            onFavPlaceClick(lat, lon)
                                        }
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

@Composable
fun FavCityItem(city: City, favViewModel: FavoriteViewModel, onCardClick: (Double, Double)-> Unit) {
    val countryName = getCountryName(city.country)
    val lat = city.coord.lat
    val lon = city.coord.lon
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete City", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Are you sure you want to delete ${city.name}? 🗑️❌")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text("This action cannot be undone. 😞")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        favViewModel.removeFavoriteCity(city)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Yes, Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onCardClick(lat, lon) },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = countryName,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    //fontFamily = FontFamily(Font(R.font.alfa_slab)),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier
                        .clickable {
                            showDeleteDialog = true

                            //favViewModel.removeFavoriteCity(city)
                        }
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = city.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    //fontFamily = FontFamily(Font(R.font.alfa_slab)),
//                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.favorite),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}