package com.mariammuhammad.climate

import com.google.android.gms.maps.model.LatLng
import com.mariammuhammad.climate.favorite.viewmodel.FavoriteViewModel
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.model.data.Coord
import com.mariammuhammad.climate.model.data.NextDaysWeather
import com.mariammuhammad.climate.utiles.Response
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.any
import org.junit.After
import org.junit.Before
import org.junit.Test
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class FavoriteViewModelTest {
//
//    private lateinit var viewModel: FavoriteViewModel
//    private lateinit var repository: WeatherRepository
//    private val testDispatcher = StandardTestDispatcher()
//
//    val testCity = City(
//        id = 5128581,
//        name = "Berlin",
//        coord = Coord(lon = 13.4050, lat = 52.5200),
//        country = "DE",
//        population = 123456,
//        timezone = 3600,
//        sunrise = 123,
//        sunset = 456
//    )
//
//
//    private val testCities = listOf(testCity)
//
//    private val testNextDaysWeather = NextDaysWeather(
//        city = testCity,
//        list = emptyList()
//    )
//
//    @Before
//    fun setup() {
////        // mock log
////        mockkStatic(android.util.Log::class)
////        every { android.util.Log.i(any(), any()) } returns 0
//
//        Dispatchers.setMain(testDispatcher)
//
//        repository = mockk()
//        viewModel = FavoriteViewModel(repository)
//    }
//
//    @After
//    fun close() {
//        Dispatchers.resetMain()
//        unmockkAll()
//        testDispatcher.scheduler.advanceUntilIdle()
//
//    }
//
//    @Test
//    fun `getRemote5Days3HoursWeather emits Success when repository succeeds`() = runTest {
//        // Given
//        coEvery { repository.get5DaysEvery3Hours(any(), any(), any(), any()) } returns flowOf(testNextDaysWeather)
//
//        // When
//        viewModel.getRemote5Days3HoursWeather(52.5200, 13.4050, "metric", "en")
//
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Then
//        val result = viewModel.fiveDayFavoriteCity.value
//        assertTrue("Expected Success but got $result", result is Response.Success)
//        assertEquals(testNextDaysWeather, (result as Response.Success).data)
//        coVerify { repository.get5DaysEvery3Hours(52.5200, 13.4050, "metric", "en") }
//    }
//
//    @Test
//    fun `getRemote5Days3HoursWeather emits Failure when repository fails`() = runTest {
//        // Given
//        val error = Throwable("Network error")
//        coEvery { repository.get5DaysEvery3Hours(any(), any(), any(), any()) } returns flow { throw error }
//
//        // When
//        viewModel.getRemote5Days3HoursWeather(52.5200, 13.4050, "metric", "en")
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Then
//        val result = viewModel.fiveDayFavoriteCity.value
//        assertTrue("Expected Failure but got $result", result is Response.Failure)
//        assertEquals("Network error", (result as Response.Failure).error.message)
//    }
//
//    @Test
//    fun `addFavoriteCity updates state when successful`() = runTest {
//        // Given
//        coEvery { repository.addFavCity(any()) } returns 1L
//        coEvery { repository.getAllFavCities() } returns flowOf(listOf(testCity))
//
//        // When
//        viewModel.addFavoriteCity(testCity)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Then
//        val result = viewModel.favoriteCities.value
//        assertTrue("Expected Success but got $result", result is Response.Success)
//        assertEquals(1, (result as Response.Success).data.size)
//        assertEquals(testCity, (result as Response.Success).data[0])
//    }
//
//    @Test
//    fun `addFavoriteCity emits Failure when repository fails`() = runTest {
//        // Given
//        val error = Throwable("Database error")
//        coEvery { repository.addFavCity(any()) } throws error
//
//        // When
//        viewModel.addFavoriteCity(testCity)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Then
//        val result = viewModel.favoriteCities.value
//        assertTrue("Expected Failure but got $result", result is Response.Failure)
//        assertEquals("Database error", (result as Response.Failure).error.message)
//    }
//
//    @Test
//    fun `getFavCities emits Success when repository succeeds`() = runTest {
//        // Given
//        coEvery { repository.getAllFavCities() } returns flowOf(testCities)
//
//        // When
//        viewModel.getFavCities()
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Then
//        val result = viewModel.favoriteCities.value
//        assertTrue("Expected Success but got $result", result is Response.Success)
//        assertEquals(testCities, (result as Response.Success).data)
//    }
//
//    @Test
//    fun `getFavCities emits Failure when repository fails`() = runTest {
//        // Given
//        val error = Throwable("Database error")
//        coEvery { repository.getAllFavCities() } returns flow { throw error }
//
//        // When
//        viewModel.getFavCities()
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Then
//        val result = viewModel.favoriteCities.value
//        assertTrue("Expected Failure but got $result", result is Response.Failure)
//        assertEquals("Database error", (result as Response.Failure).error.message)
//    }
//}