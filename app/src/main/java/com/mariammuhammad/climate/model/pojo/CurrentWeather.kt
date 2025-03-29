package com.mariammuhammad.climate.model.pojo

import com.google.gson.annotations.SerializedName

    data class CurrentWeather(
        var weather: List<Weather>,
        @SerializedName("main") var weatherDetails: Main,
        var wind: Wind,
        var clouds: Clouds,
        var dt: Int = 0,
        var sys: Sys,
        @SerializedName("name")
        var cityName: String?,
        var timezone: Int = 0
    )
