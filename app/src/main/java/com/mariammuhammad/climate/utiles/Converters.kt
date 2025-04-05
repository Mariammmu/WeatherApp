package com.mariammuhammad.climate.utiles

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.model.data.Clouds
import com.mariammuhammad.climate.model.data.ListDaysDetails
import com.mariammuhammad.climate.model.data.Main
import com.mariammuhammad.climate.model.data.Sys
import com.mariammuhammad.climate.model.data.Weather
import com.mariammuhammad.climate.model.data.Wind

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

class CloudsConverter {

    @TypeConverter
    fun fromClouds(clouds: Clouds): String {
        return Gson().toJson(clouds)
    }

    @TypeConverter
    fun toClouds(json: String): Clouds {
        return Gson().fromJson(json, Clouds::class.java)
    }
}

class MainConverter {

    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(json: String): Main {
        return Gson().fromJson(json, Main::class.java)
    }
}

class SysConverter {

    @TypeConverter
    fun fromSys(sys: Sys): String {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toSys(json: String): Sys {
        return Gson().fromJson(json, Sys::class.java)
    }
}

class WeatherConverter {

    @TypeConverter
    fun fromWeather(weatherList: List<Weather>): String {
        return Gson().toJson(weatherList)
    }

    @TypeConverter
    fun toWeather(json: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(json, type)
    }
}

class WindConverter {

    @TypeConverter
    fun fromWind(wind: Wind): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toWind(json: String): Wind {
        return Gson().fromJson(json, Wind::class.java)
    }
}








