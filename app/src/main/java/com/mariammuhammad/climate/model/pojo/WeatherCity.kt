package com.mariammuhammad.climate.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Root(
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ListDaysDetails>,
    val city: City,
)

data class ListDaysDetails(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Long,
    val pop: Long,
    val sys: Sys,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val pod: PeriodOfDay,
    val rain: Rain
)

data class PeriodOfDay(
    val pod: String, //period Of time
)

data class Rain(
    @SerializedName("3h")
    val rainLastThreeHours: Double
)

@Entity(tableName = "favorites")
data class City(
   @PrimaryKey val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long,
    val timezone: Long,
    val sunrise: Long,
    val sunset: Long,
)

