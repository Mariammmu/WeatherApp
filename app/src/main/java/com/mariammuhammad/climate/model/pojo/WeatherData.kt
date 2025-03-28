package com.mariammuhammad.climate.model.pojo

import com.google.gson.annotations.SerializedName
//https://openweathermap.org/forecast5
//Endpoint
//http://api.openweathermap.org/data/2.5/forecast?id=524901&lang=zh_cn&appid={API key}
data class Root(
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ListItem>,
    val city: City,
)


data class ListItem(
    val dt: Long, //date and time
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Long,
    val pop: Double,
    val rain: Rain?,
    val sys: Sys,
    @SerializedName("dt_txt")
    val dtTxt: String, //date and time string
    val snow: Snow?,
)

data class Main(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Long,
    @SerializedName("sea_level")
    val seaLevel: Long,
    @SerializedName("grnd_level")
    val grndLevel: Long,
    val humidity: Long,
    @SerializedName("temp_kf")
    val tempKf: Long,
)

data class Weather(
    @SerializedName("value")
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
)

data class Clouds(
    val all: Long,
)

data class Wind(
    val speed: Double,
    val deg: Long,
    val gust: Double,
)

data class Rain(
    @SerializedName("3h")
    val n3h: Double, //rain every 3 hours
)

data class Sys(
    val pod: String, //period of day
)

data class Snow(
    @SerializedName("3h")
    val n3h: Double,
)

data class City(
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long,
    val timezone: Long,
    val sunrise: Long,
    val sunset: Long,
)

data class Coord(
    val lat: Double,
    val lon: Double,
)

/*
//endpoint
//api.openweathermap.org/data/2.5/weather?q=London&appid={API key}
data class Root(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Long,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Long,
    val id: Long,
    val name: String,
    val cod: Long,
)

data class Coord(
    val lon: Double,
    val lat: Double,
)

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
)

data class Main(
    val temp: Double,
     @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Long,
    val humidity: Long,
    @SerializedName("sea_level")
    val seaLevel: Long,
    @SerializedName("grnd_level")
    val grndLevel: Long,
)

data class Wind(
    val speed: Double,
    val deg: Long,
)

data class Clouds(
    val all: Long,
)

data class Sys(
    val type: Long,
    val id: Long,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)


*/