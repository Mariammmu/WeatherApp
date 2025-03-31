package com.mariammuhammad.climate.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

data class NextDaysWeather (
    val list: List<CurrentWeather>
   // val city: City
    )
