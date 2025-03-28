package com.mariammuhammad.climate.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather_table")
data class NextDaysWeather (
    val list: List<ListItem>,
    @PrimaryKey
    val city: City
    )
