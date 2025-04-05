package com.mariammuhammad.climate.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey val id: Int,
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)