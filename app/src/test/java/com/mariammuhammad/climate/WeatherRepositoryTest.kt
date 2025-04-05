package com.mariammuhammad.climate

import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.local.WeatherLocalDataSource
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.model.data.Coord
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private lateinit var remoteDataSource: WeatherRemoteDataSource
    private lateinit var localDataSource: WeatherLocalDataSource
    private lateinit var repository: WeatherRepository

//    private val testCoord = Coord(lon = 13.4050, lat = 52.5200)
//    val testMain = Main(
//        temp = 20.0,
//        pressure = 1012,
//        humidity = 80,
//        feelsLike = 18.0,
//        tempMin = 18.0,
//        tempMax = 22.0,
//        seaLevel = 1010,
//        grndLevel = 1005,
//        tempKf = 0.0
//    )
//    private val testWeather = listOf( Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d"))
//    private val testWind = Wind(speed = 5.0, deg = 180, gust =6.0 )
//    private val testClouds = Clouds(all = 10)
//    private val testSys = Sys(country = "DE", sunrise = 1672500000, sunset = 1672550000)
//
//    private val testCurrentWeather = CurrentWeather(
//        base = "stations",
//        clouds = testClouds,
//        cod = 200,
//        coord = testCoord,
//        dt = 1672531200,
//        id = 5128581,
//        main = testMain,
//        name = "Berlin",
//        sys = testSys,
//        timezone = 3600,
//        visibility = 10000,
//        weather = testWeather, //listOf(Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
//        wind = testWind
//    )

    private val testCity = City(
        id = 5128581,
        name = "Berlin",
        coord =  Coord(lon = 13.4050, lat = 52.5200),
        country = "DE",
        population = 3645000,
        timezone = 3600,
        sunrise = 1672500000,
        sunset = 1672550000
    )
    @Before
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        repository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
    }

//    @Test
//    fun `getWeatherForecast should emit current weather from remote source`() = runTest {
//        // Given
//        coEvery { remoteDataSource.getCurrentWeather(any(), any(), any(), any()) } returns flowOf(testCurrentWeather)
//
//        // When
//        val result = repository.getWeatherForecast(52.5200, 13.4050, "metric", "en")
//
//        // Then
//        assertEquals(testCurrentWeather, result.first())
//        coVerify { remoteDataSource.getCurrentWeather(52.5200, 13.4050, "metric", "en") }
//    }



    @Test
    fun `addFavCity should delegate to local source and return rowId`() = runTest {
        // Given
        coEvery { localDataSource.addFavCity(any()) } returns 1L

        // When
        val result = repository.addFavCity(testCity)

        // Then
        assertEquals(1L, result)
        coVerify { localDataSource.addFavCity(testCity) }
    }

    @Test
    fun `deleteFavCity should delegate to local source and return rowsAffected`() = runTest {
        // Given
        coEvery { localDataSource.deleteFavCity(any()) } returns 1

        // When
        val result = repository.deleteFavCity(testCity)

        // Then
        assertEquals(1, result)
        coVerify { localDataSource.deleteFavCity(testCity) }
    }



}