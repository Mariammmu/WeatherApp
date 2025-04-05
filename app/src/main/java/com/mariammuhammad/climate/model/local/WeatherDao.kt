package com.mariammuhammad.climate.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCurrentWeather(weatherResponse: NextDaysWeather)

    @Query("SELECT * FROM weather")
    fun getStoredWeather(): Flow<NextDaysWeather>

}