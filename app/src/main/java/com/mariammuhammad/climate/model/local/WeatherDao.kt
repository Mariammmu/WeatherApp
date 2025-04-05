package com.mariammuhammad.climate.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.NextDaysWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNextDaysWeather(weatherResponse: NextDaysWeather)

    @Query("SELECT * FROM weather")
    fun getStoredWeather(): Flow<NextDaysWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather")
    fun getCurrentWeather(): Flow<CurrentWeather>

}