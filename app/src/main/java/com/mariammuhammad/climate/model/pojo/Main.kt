package com.mariammuhammad.climate.model.pojo

import com.google.gson.annotations.SerializedName

data class Main(
    val temp: Double,
    val pressure: Long,
    val humidity: Long,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("sea_level")
    val seaLevel: Long,
    @SerializedName("grnd_level")
    val grndLevel: Long,
    @SerializedName("temp_kf")
    val tempKf: Double,
)

