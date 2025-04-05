package com.mariammuhammad.climate.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mariammuhammad.climate.model.data.City
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun getFavCities(): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavCity(city: City) : Long

    @Delete
    suspend fun deleteFavCity(city: City) : Int
}