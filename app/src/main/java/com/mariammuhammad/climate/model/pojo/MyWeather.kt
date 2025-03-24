package com.mariammuhammad.climate.model.pojo

class MyWeather {
    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String,
    )
}