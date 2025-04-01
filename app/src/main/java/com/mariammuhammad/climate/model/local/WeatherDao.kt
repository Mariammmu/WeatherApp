package com.mariammuhammad.climate.model.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import kotlinx.coroutines.flow.Flow

interface WeatherDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAllCurrentWeather(weatherResponse: NextDaysWeather) : Long
//
//    fun getStoredWeather(): Flow<NextDaysWeather>
//
//    suspend fun deleteAllWeather()
}