package com.mariammuhammad.climate.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mariammuhammad.climate.R

class SettingsScreen {
    @Composable
    fun SettingsScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background))
                .padding(16.dp)
        ) {
            SettingOption(
                title = "Language",
                icon = Icons.Default.Done,
                options = listOf("Arabic", "English", "Default"),
                selectedOption = "English"
            )

            SettingOption(
                title = "Temp Unit",
                //Question
                icon = Icons.Default.ArrowDropDown,
                options = listOf("Celsius °C", "Kelvin °K", "Fahrenheit °F"),
                selectedOption = "Celsius °C"
            )

            SettingOption(
                title = "Location",
                icon = Icons.Default.LocationOn,
                options = listOf("GPS", "Map"),
                selectedOption = "Map"
            )

            SettingOption(
                title = "Wind Speed Unit",
//                icon= Icon(
//                    painter = painterResource(id = R.drawable.ic_add_location), // Use your custom vector image here
//                    contentDescription = "Add location")
                Icons.Default.Place,
                options = listOf("meter/sec", "mile/hour"),
                selectedOption = "meter/sec"
            )
        }
    }

    @Composable
    fun SettingOption(title: String, icon: ImageVector, options: List<String>, selectedOption: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.violet))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = title,
                        tint = colorResource(R.color.off_white))
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(text = title, color = colorResource(R.color.off_white),
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Handle selection */ }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = { /* Handle selection */ },
                            colors = RadioButtonDefaults.colors(selectedColor = colorResource(R.color.icy))
                        )
                        Text(text = option, color = colorResource(R.color.off_white), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}