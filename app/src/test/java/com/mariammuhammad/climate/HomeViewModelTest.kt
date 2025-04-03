package com.mariammuhammad.climate
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.pojo.CurrentWeather
import com.mariammuhammad.climate.model.pojo.NextDaysWeather
import com.mariammuhammad.climate.utiles.Response
import com.mariammuhammad.climate.home.viewmodel.HomeViewModel
import com.mariammuhammad.climate.model.pojo.City
import com.mariammuhammad.climate.model.pojo.Clouds
import com.mariammuhammad.climate.model.pojo.Coord
import com.mariammuhammad.climate.model.pojo.ListDaysDetails
import com.mariammuhammad.climate.model.pojo.Main
import com.mariammuhammad.climate.model.pojo.PeriodOfDay
import com.mariammuhammad.climate.model.pojo.Rain
import com.mariammuhammad.climate.model.pojo.Sys
import com.mariammuhammad.climate.model.pojo.Weather
import com.mariammuhammad.climate.model.pojo.Wind
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var weatherRepository: WeatherRepository
    private val testDispatcher = StandardTestDispatcher()

    val mockMain = Main(
        temp = 20.0,
        pressure = 1012,
        humidity = 80,
        feelsLike = 18.0,
        tempMin = 18.0,
        tempMax = 22.0,
        seaLevel = 1010,
        grndLevel = 1005,
        tempKf = 0.0
    )

    val mockCurrentWeather = CurrentWeather(
        base = "stations",
        clouds = Clouds(all = 10),
        cod = 200,
        coord = Coord(lon = 13.4050, lat = 52.5200),
        dt = 1672531200,
        id = 5128581,
        main = mockMain,
        name = "Berlin",
        sys = Sys(country = "DE", sunrise = 1672500000, sunset = 1672550000),
        timezone = 3600,
        visibility = 10000,
        weather = listOf(Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
        wind = Wind(deg = 180, gust = 5.0, speed = 5.0)
    )

    val mockNextDaysWeather = NextDaysWeather(
        list = listOf(
            ListDaysDetails(
                dt = 1672632000,
                main = mockMain,
                weather = listOf(Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
                wind = Wind(deg = 180, gust = 5.0, speed = 5.0),
                clouds = Clouds(all = 0),
                visibility = 10000,
                pop = 0,
                sys = Sys(country = "DE", sunrise = 1672500000, sunset = 1672550000),
                dtTxt = "2025-04-02 15:00:00",
                pod = PeriodOfDay("morning"),
                rain = Rain(1.0)
            )
        ),
        city = City(id = 5128581, name = "Berlin", coord = Coord(lon = 13.4050, lat = 52.5200), country = "DE", population = 123456, timezone = 3600, sunrise = 123, sunset = 456)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        weatherRepository = mockk()

        homeViewModel = HomeViewModel(weatherRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrentWeather should emit Success when repository succeeds`() = runTest {
        // Given
        coEvery { weatherRepository.getWeatherForecast(any(), any(), any(), any()) } returns flowOf(mockCurrentWeather)

        // When -call the method
        homeViewModel.getCurrentWeather(52.5200, 13.4050, "metric", "en")
        advanceUntilIdle() //wait for coroutines to complete

        // Then - Verify state and repository call
        val result = homeViewModel.currentWeather.value
        assertTrue(result is Response.Success)
        assertEquals(mockCurrentWeather, (result as Response.Success).data)
        coVerify { weatherRepository.getWeatherForecast(52.5200, 13.4050, "metric", "en") }
    }
    

    @Test
    fun `get5DaysWeather should emit Success when repository succeeds`() = runTest {
        // Given
        coEvery { weatherRepository.get5DaysEvery3Hours(any(), any(), any(), any()) } returns flowOf(mockNextDaysWeather)

        // When
        homeViewModel.get5DaysWeather(52.5200, 13.4050, "metric", "en")
        advanceUntilIdle()

        // Then -verify state and repository call
        val result = homeViewModel.nextDaysWeather.value
        assertTrue(result is Response.Success)
        assertEquals(mockNextDaysWeather, (result as Response.Success).data)
        coVerify { weatherRepository.get5DaysEvery3Hours(52.5200, 13.4050, "metric", "en") }
    }


}

