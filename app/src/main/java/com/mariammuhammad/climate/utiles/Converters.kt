package com.mariammuhammad.climate.utiles

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.model.pojo.Coord
import com.mariammuhammad.climate.model.pojo.ListDaysDetails
import com.mariammuhammad.climate.model.pojo.Main
import com.mariammuhammad.climate.model.pojo.Weather

class Converters {

    @TypeConverter
    fun fromListDaysDetails(list: List<ListDaysDetails>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListDaysDetails(json: String): List<ListDaysDetails> {
        val gson = Gson()
        val type = object : TypeToken<List<ListDaysDetails>>() {}.type
        return gson.fromJson(json, type)
    }
}

class CityConverter {

    @TypeConverter
    fun fromString(value: String): City {
        return Gson().fromJson(value, City::class.java)
    }

    @TypeConverter
    fun fromCity(city: City): String {
        return Gson().toJson(city)
    }
}
//    @TypeConverter
//    fun fromWeatherList(weatherList: List<Weather>): String {
//        return Gson().toJson(weatherList)
//    }
//
//    @TypeConverter
//    fun toWeatherList(weatherListString: String): List<Weather> {
//        val listType = object : TypeToken<List<Weather>>() {}.type
//        return Gson().fromJson(weatherListString, listType)
//    }
//
//    @TypeConverter
//    fun fromMain(main: Main): String {
//        return Gson().toJson(main)
//    }
//
//    @TypeConverter
//    fun toMain(mainString: String): Main {
//        return Gson().fromJson(mainString, Main::class.java)
//    }
//
//
//    @TypeConverter
//    fun fromString(value: String): Coord {
//        val parts = value.split(",")
//        return Coord(
//            lon = parts[0].toDouble(),
//            lat = parts[1].toDouble()
//        )
//    }
//
//    @TypeConverter
//    fun toString(coordinates: Coord): String {
//        return "${coordinates.lon},${coordinates.lat}"
//    }
