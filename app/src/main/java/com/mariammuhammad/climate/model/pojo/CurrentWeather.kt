package com.mariammuhammad.climate.model.pojo

import com.google.gson.annotations.SerializedName

    data class CurrentWeather(
        var weather: List<Weather>,
        @SerializedName("main") var weatherDetails: Main,
        var wind: Wind? = null,
        var clouds: Clouds? = null,
        var dt: Int = 0,
        var sys: Sys? = null,
        @SerializedName("name")
        var cityName: String? = null,
        var timezone: Int = 0
    )
