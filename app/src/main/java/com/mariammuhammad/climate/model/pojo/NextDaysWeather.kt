package com.mariammuhammad.climate.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mariammuhammad.climate.utiles.CityConverter
import com.mariammuhammad.climate.utiles.Converters

@Entity(tableName = "weather")
data class NextDaysWeather(
    val list: List<ListDaysDetails> = listOf(),
    @PrimaryKey val city: City
)