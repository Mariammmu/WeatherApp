package com.mariammuhammad.climate

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.mariammuhammad.climate.model.data.Alarm
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.model.data.Coord
import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.NextDaysWeather
import com.mariammuhammad.climate.model.local.AlarmDao
import com.mariammuhammad.climate.model.local.FavoritesDao
import com.mariammuhammad.climate.model.local.IWeatherLocalDataSource
import com.mariammuhammad.climate.model.local.WeatherDao
import com.mariammuhammad.climate.model.local.WeatherDataBase
import com.mariammuhammad.climate.model.local.WeatherLocalDataSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceTest {

    private lateinit var database: WeatherDataBase
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var weatherDao: WeatherDao
    private lateinit var alarmDao: AlarmDao
    private lateinit var localDataSource: WeatherLocalDataSource

    private val testCity = City(
        id = 1,
        name = "Berlin",
        coord = Coord(lon = 13.4050, lat = 52.5200),
        country = "DE",
        population = 3645000,
        timezone = 3600,
        sunrise = 123456,
        sunset = 654321
    )

    private val testAlarm = Alarm(
        cityName = "Berlin",
        hour = 2,
        minute = 10
    )

    @Before
    fun setup() {
        // Create in-memory database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).allowMainThreadQueries().build()

        favoritesDao = database.getFavoritesDao()
        weatherDao = database.getWeatherDao()
        alarmDao = database.getAlarmDao()
        localDataSource = WeatherLocalDataSource(favoritesDao, weatherDao, alarmDao)
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun getAllFavCities_returnsFlowOfCities() = runTest {
        // Given
        favoritesDao.insertFavCity(testCity)

        // When
        val cities = localDataSource.getAllFavCities().first()

        // Then
        assertEquals(1, cities.size)
        assertEquals(testCity, cities[0])
    }

    @Test
    fun addFavCity_returnsRowId() = runTest {
        // When
        val rowId = localDataSource.addFavCity(testCity)

        // Then
        assertTrue(rowId > 0)
    }

    @Test
    fun deleteFavCity_returnsDeleteCount() = runTest {
        // Given
        favoritesDao.insertFavCity(testCity)

        // When
        val deleteCount = localDataSource.deleteFavCity(testCity)

        // Then
        assertEquals(1, deleteCount)
    }

//    @Test
//    fun getAllAlarms_returnsFlowOfAlarms() = runTest {
//        // Given
//        alarmDao.addAlarm(testAlarm)
//
//        // When
//        val alarms = localDataSource.getAllAlarms().first()
//
//        // Then
//        assertEquals(1, alarms.size)
//        assertEquals(testAlarm, alarms[0])
//    }
//
//    @Test
//    fun addAlarm_returnsRowId() = runTest {
//        // When
//        val rowId = localDataSource.addAlarm(testAlarm)
//
//        // Then
//        assertTrue(rowId > 0)
//    }
//
//    @Test
//    fun deleteAlarm_returnsDeleteCount() = runTest {
//        // Given
//        alarmDao.addAlarm(testAlarm)
//
//        // When
//        val deleteCount = localDataSource.deleteAlarm(testAlarm)
//
//        // Then
//        assertEquals(1, deleteCount)
//    }
//
//    @Test
//    fun deleteAlarm_removesFromDatabase() = runTest {
//        // Given
//        alarmDao.addAlarm(testAlarm)
//
//        // When
//        localDataSource.deleteAlarm(testAlarm)
//        val alarms = localDataSource.getAllAlarms().first()
//
//        // Then
//        assertTrue(alarms.isEmpty())
//    }
}