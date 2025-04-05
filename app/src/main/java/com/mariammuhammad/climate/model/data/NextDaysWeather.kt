package com.mariammuhammad.climate.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class NextDaysWeather(
    val list: List<ListDaysDetails> = listOf(),
    @PrimaryKey val city: City
)