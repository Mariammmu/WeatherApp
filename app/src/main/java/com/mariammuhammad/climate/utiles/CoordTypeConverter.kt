package com.mariammuhammad.climate.utiles

import androidx.room.TypeConverter
import com.mariammuhammad.climate.model.pojo.Coord

class CoordTypeConverter {

    @TypeConverter
    fun fromString(value: String): Coord {
        val parts = value.split(",")
        return Coord(
            lon = parts[0].toDouble(),
            lat = parts[1].toDouble()
        )
    }

    @TypeConverter
    fun toString(coordinates: Coord): String {
        return "${coordinates.lon},${coordinates.lat}"
    }
}
