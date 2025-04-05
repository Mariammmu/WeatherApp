package com.mariammuhammad.climate

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.mariammuhammad.climate.model.local.FavoritesDao
import com.mariammuhammad.climate.model.local.WeatherDataBase
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.model.data.Coord
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoritesDaoTest {

    private lateinit var database: WeatherDataBase
    private lateinit var dao: FavoritesDao

    @Before
    fun setup() {
        // Create in-memory database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).allowMainThreadQueries().build()

        dao = database.getFavoritesDao()
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun insertFavCityAndGetAll() = runTest {
        // Given
        val city = City(
            id = 1,
            name = "Berlin",
            coord = Coord(lon = 13.4050, lat = 52.5200),
            country = "DE",
            population = 3645000,
            timezone = 3600,
            sunrise = 123456,
            sunset = 654321
        )

        // When
        dao.insertFavCity(city)
        val cities = dao.getFavCities().first()

        // Then
        assertEquals(1, cities.size)
        assertEquals(city, cities[0])
    }

    @Test
    fun deleteFavCity_removesFromDatabase() = runTest {
        // Given
        val city = City(id = 1,
            name = "Berlin",
            coord = Coord(lon = 13.4050, lat = 52.5200),
            country = "DE",
            population = 3645000,
            timezone = 3600,
            sunrise = 123456,
            sunset = 654321)

        dao.insertFavCity(city)

        // When
        val deleteCount = dao.deleteFavCity(city)
        val cities = dao.getFavCities().first()

        // Then
        assertEquals(1, deleteCount)
        assertTrue(cities.isEmpty())
    }


}