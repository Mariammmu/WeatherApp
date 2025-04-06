package com.mariammuhammad.climate.settings.view

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.settings.viewmodel.SettingsViewModel
import com.mariammuhammad.climate.utiles.Constants
import com.mariammuhammad.climate.utiles.LanguageUtils
import com.mariammuhammad.climate.utiles.Response


@SuppressLint("SuspiciousIndentation")
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onMapOptionClick: () -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity
    val languageState by viewModel.language.collectAsState()
    val temperatureUnitState = viewModel.temperatureUnit.collectAsState()
    val locationState by viewModel.locationFinder.collectAsState()
    val windSpeedUnitState by viewModel.windSpeedUnit.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Language selection card
        when (languageState) {
            is Response.Loading -> {
                CircularProgressIndicator()
            }

            is Response.Success -> {
                val currentLang = (languageState as Response.Success<String>).data
                val displayLang = when (currentLang) {
                    LanguageUtils.ARABIC -> stringResource(R.string.arabic)
                    LanguageUtils.ENGLISH -> stringResource(R.string.english)
                    else -> stringResource(R.string.default_lang)



                }
                SettingsCard(
                    title = stringResource(R.string.language),
                    icon = painterResource(id = R.drawable.language_translate_bubbles_icon),
                    options = listOf(
                        stringResource(R.string.arabic),
                        stringResource(R.string.english),
                        stringResource(R.string.default_lang)
                    ),
                    selectedOption = displayLang, //stringResource(R.string.english),
                    onOptionSelected = { language ->
                        val langCode= when(language){
                            "Arabic" -> LanguageUtils.ARABIC //stringResource(R.string.arabic)
                            "English" -> LanguageUtils.ENGLISH
                            else -> "Default"
                        }
                        //LanguageUtils.changeLanguage(activity, getLanguageCode(language))
                        LanguageUtils.changeLanguage(activity, langCode)
                        viewModel.saveLanguage(langCode)


                    }
                )
            }

            is Response.Failure -> {

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = temperatureUnitState.value) {
            is Response.Loading -> {
                CircularProgressIndicator()
            }

            is Response.Success -> {
                val currentUnit = state.data
                val displayOptions = listOf(
                    stringResource(R.string.celsius_c) to Constants.UNITS_CELSIUS,
                    stringResource(R.string.fahrenheit_f) to Constants.UNITS_FAHRENHEIT,
                    stringResource(R.string.kelvin_k) to Constants.UNITS_DEFAULT
                )

                val currentDisplay = displayOptions.find { it.second == currentUnit }?.first
                    ?: stringResource(R.string.celsius_c)

                SettingsCard(
                    title = stringResource(R.string.temp_unit),
                    icon = painterResource(id = R.drawable.speedometer_color_icon),
                    options = displayOptions.map { it.first },
                    selectedOption = currentDisplay,
                    onOptionSelected = { selectedDisplay ->

                        val selectedUnit =
                            displayOptions.find { it.first == selectedDisplay }?.second
                                ?: Constants.UNITS_CELSIUS
                        viewModel.saveTemperatureUnit(selectedUnit)
                    }
                )
            }

            is Response.Failure -> {
                Text("Error loading temperature settings", color = Color.Red)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        when (locationState) {
            is Response.Loading -> {
                CircularProgressIndicator()
            }

            is Response.Success -> {
                val currentMethod = (locationState as Response.Success<String>).data
                val displayMethod = when (currentMethod.lowercase()) {
                    "gps" -> stringResource(R.string.gps)
                    "map" -> stringResource(R.string.map)
                    else -> stringResource(R.string.gps)
                }

                SettingsCard(
                    title = stringResource(R.string.location),
                    icon = painterResource(id = R.drawable.google_map_icon),
                    options = listOf(
                        stringResource(R.string.gps),
                        stringResource(R.string.map)
                    ),
                    selectedOption = displayMethod,
                    onOptionSelected = { method ->
                        if (method == "Map") { // stringResource(R.string.map)) {
                            onMapOptionClick()
                        } else {
                            viewModel.saveLocationFinder("gps")
                        }
                    }
                )
            }

            is Response.Failure -> {
                Text("Error loading location settings", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (windSpeedUnitState) {
            is Response.Loading -> {
                CircularProgressIndicator()
            }

            is Response.Success -> {
                val currentUnit = (windSpeedUnitState as Response.Success<String>).data
                val displayOptions = listOf(
                    stringResource(R.string.meter_sec) to "m/s",
                    stringResource(R.string.mile_hour) to "mph"
                )

                val currentDisplay = displayOptions.find { it.second == currentUnit }?.first
                    ?: stringResource(R.string.meter_sec)

                SettingsCard(
                    title = stringResource(R.string.wind_speed_unit),
                    icon = painterResource(id = R.drawable.cloud_wind_color_icon),
                    options = displayOptions.map { it.first },
                    selectedOption = currentDisplay,
                    onOptionSelected = { selectedDisplay ->
                        val selectedUnit =
                            displayOptions.find { it.first == selectedDisplay }?.second
                                ?: "m/s"
                        viewModel.saveWindSpeedUnit(selectedUnit)
                    }
                )
            }

            is Response.Failure -> {
                Text("Error loading wind speed settings", color = Color.Red)
            }
        }
    }
}

//private fun getLanguageCode(language: String): String {
//    return when {
//
//        language == "Arabic" -> {
//            LanguageUtils.ARABIC
//        }
//
//        language == "English" -> {
//            LanguageUtils.ENGLISH
//        }
//
//        else -> "Default"
//    }
//
//
//}

@Composable
private fun SettingsCard(
    title: String,
    icon: Painter,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = Color.Unspecified,
                    //tint = colorResource(R.color.dark_purple),
                    modifier = Modifier.size(24.dp),

                    )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily(Font(R.font.alfa_slab)),
                    color = colorResource(R.color.off_white)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                // horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                        //  .clickable { onOptionSelected(option) }
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(R.color.dark_purple),
                                unselectedColor = Color.Gray
                            )
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(R.color.off_white),
                            fontSize = 10.sp,
                        )
                    }
                }
            }
        }
    }
}
