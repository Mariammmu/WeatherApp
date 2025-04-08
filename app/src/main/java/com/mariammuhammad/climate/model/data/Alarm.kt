package com.mariammuhammad.climate.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey val id: String=UUID.randomUUID().toString() ,
    // (autoGenerate = true) auto generate will make the data base create the id for you
    val cityName: String,
   // val lat: Double,
    //val lon: Double,
    val hour: Int,
    val minute: Int,
)