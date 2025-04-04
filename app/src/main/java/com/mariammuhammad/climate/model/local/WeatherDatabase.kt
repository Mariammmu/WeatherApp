package com.mariammuhammad.climate.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mariammuhammad.climate.model.pojo.Alarm
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.utiles.CityConverter
import com.mariammuhammad.climate.utiles.Converters
import com.mariammuhammad.climate.utiles.CoordTypeConverter

@Database(entities = [City::class, NextDaysWeather::class,Alarm::class], version = 2)
//@TypeConverters(Converters::class)
@TypeConverters(Converters::class, CityConverter::class,CoordTypeConverter::class)

    abstract class WeatherDataBase: RoomDatabase() {

        abstract fun getFavoritesDao():FavoritesDao
        abstract fun getWeatherDao(): WeatherDao
        abstract fun getAlarmDao():AlarmDao

        companion object{ // store variables or functions that are shared among all instances of the class.
            @Volatile //the value of INSTANCE is updated across all threads, which is important for thread safety.
            private var INSTANCE: WeatherDataBase? = null

            fun  getInstance(context: Context): WeatherDataBase{

                return  INSTANCE?: synchronized(this) { //only one thread can access the code inside it at a time,
                    // which is important for thread safety.
                    // It's needed to prevent multiple threads from trying to create the WeatherDataBase instance simultaneously.
                    val instance = Room.databaseBuilder( //creates the actual Room database. It takes: 3 params
                        context.applicationContext,
                        WeatherDataBase::class.java,"weather_database")
                        .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
