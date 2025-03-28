package com.mariammuhammad.climate.model.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import kotlinx.coroutines.flow.Flow

interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCurrentWeather(weatherResponse: NextDaysWeather)

    @Query("SELECT * FROM current_weather_table")
    fun getStoredWeather(): Flow<NextDaysWeather>

    @Query("DELETE FROM current_weather_table")
    suspend fun deleteAllWeather()
}