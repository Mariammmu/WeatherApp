package com.mariammuhammad.climate.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey val id: Int ,
    val cityName: String,
   // val lat: Double,
    //val lon: Double,
    val hour: Int,
    val minute: Int,
)