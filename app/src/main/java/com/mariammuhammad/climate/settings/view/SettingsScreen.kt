package com.mariammuhammad.climate.settings.view

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: com.mariammuhammad.climate.settings.view.SettingsViewModel) {
    // val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Language Card
        SettingsCard(
            title = "Language",
            icon = painterResource(id = R.drawable.language_translate_bubbles_icon),
            options = listOf("Arabic", "English", "Default"),
            selectedOption = "English",//state.language,
            onOptionSelected = { viewModel.setLanguage(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Temperature Unit Card
        SettingsCard(
            title = "Temp Unit",
            icon = painterResource(id = R.drawable.speedometer_color_icon),
            options = listOf("Celsius °C", "Kelvin °K", "Fahrenheit °F"),
            selectedOption = "Celsius",//state.tempUnit,
            onOptionSelected = { viewModel.setTempUnit(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Location Card
        SettingsCard(
            title = "Location",
            icon = painterResource(id = R.drawable.google_map_icon),
            options = listOf("Gps", "Map"),
            selectedOption = "Gps",//state.locationMethod,
            onOptionSelected = { viewModel.setLocationMethod(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wind Speed Unit Card
        SettingsCard(
            title = "Wind Speed Unit",
            icon = painterResource(id = R.drawable.cloud_wind_color_icon),
            options = listOf("meter/sec", "mile/hour"),
            selectedOption = "meter/sec",//state.windSpeedUnit,
            onOptionSelected = { viewModel.setWindSpeedUnit(it) }
        )

//        if (!state.isInternetConnected) {
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "no internet connection",
//                color = Color.Red,
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
    }
}

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
            // Title with icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = colorResource(R.color.dark_purple),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily(Font(R.font.alfa_slab)),
                    color = colorResource(R.color.off_white)
                )
            }

            // Radio options
            Row(
                modifier = Modifier.fillMaxWidth(),
                // horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            // .weight(2f)
//                            .padding(horizontal = 4.dp)
                            .clickable { onOptionSelected(option) }
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

// ViewModel remains the same
class SettingsViewModel : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    fun setLanguage(language: String) {
        _state.value = _state.value.copy(language = language)
    }

    fun setTempUnit(unit: String) {
        _state.value = _state.value.copy(tempUnit = unit)
    }

    fun setLocationMethod(method: String) {
        _state.value = _state.value.copy(locationMethod = method)
    }

    fun setWindSpeedUnit(unit: String) {
        _state.value = _state.value.copy(windSpeedUnit = unit)
    }

    fun setInternetStatus(connected: Boolean) {
        _state.value = _state.value.copy(isInternetConnected = connected)
    }
}

data class SettingsState(
    val language: String = "English",
    val tempUnit: String = "Celsius °C",
    val locationMethod: String = "Gps",
    val windSpeedUnit: String = "meter/sec",
    val isInternetConnected: Boolean = true
)
//    @Composable
//    fun SettingsScreen() {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(colorResource(R.color.background))
//                .padding(16.dp)
//        ) {
//            SettingOption(
//                title = "Language",
//                icon = Icons.Default.Done,
//                options = listOf("Arabic", "English", "Default"),
//                selectedOption = "English"
//            )
//
//            SettingOption(
//                title = "Temp Unit",
//                //Question
//                icon = Icons.Default.ArrowDropDown,
//                options = listOf("Celsius °C", "Kelvin °K", "Fahrenheit °F"),
//                selectedOption = "Celsius °C"
//            )
//
//            SettingOption(
//                title = "Location",
//                icon = Icons.Default.LocationOn,
//                options = listOf("GPS", "Map"),
//                selectedOption = "Map"
//            )
//
//            SettingOption(
//                title = "Wind Speed Unit",
////                icon= Icon(
////                    painter = painterResource(id = R.drawable.ic_add_location), // Use your custom vector image here
////                    contentDescription = "Add location")
//                Icons.Default.Place,
//                options = listOf("meter/sec", "mile/hour"),
//                selectedOption = "meter/sec"
//            )
//        }
//    }
//
//    @Composable
//    fun SettingOption(title: String, icon: ImageVector, options: List<String>, selectedOption: String) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.violet))
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(imageVector = icon, contentDescription = title,
//                        tint = colorResource(R.color.off_white))
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Text(text = title, color = colorResource(R.color.off_white),
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold)
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                options.forEach { option ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { /* Handle selection */ }
//                            .padding(vertical = 4.dp)
//                    ) {
//                        RadioButton(
//                            selected = (option == selectedOption),
//                            onClick = { /* Handle selection */ },
//                            colors = RadioButtonDefaults.colors(selectedColor = colorResource(R.color.icy))
//                        )
//                        Text(text = option, color = colorResource(R.color.off_white), fontSize = 16.sp)
//                    }
//                }
//            }
//        }
//    }
