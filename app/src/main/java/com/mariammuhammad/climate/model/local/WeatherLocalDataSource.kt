package com.mariammuhammad.climate.model.local

import com.mariammuhammad.climate.model.data.Alarm
import com.mariammuhammad.climate.model.data.City
import com.mariammuhammad.climate.model.data.CurrentWeather
import com.mariammuhammad.climate.model.data.NextDaysWeather
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource (
    private val favoritesDao: FavoritesDao,
    private val weatherDao: WeatherDao,
    private val alarmDao: AlarmDao) : IWeatherLocalDataSource {

    override fun getAllFavCities(): Flow<List<City>> {
        return favoritesDao.getFavCities()
    }

    override suspend fun addFavCity(city: City):Long{
        return favoritesDao.insertFavCity(city)
    }

    override suspend fun deleteFavCity(city: City):Int{
        return favoritesDao.deleteFavCity(city)
    }

     override fun getAllStoredWeather(): Flow<NextDaysWeather> {
        return weatherDao.getStoredWeather()
    }

     override suspend fun addNextDaysWeather(weatherResponse: NextDaysWeather) {
        weatherDao.insertNextDaysWeather(weatherResponse)
    }

    override fun getCurrentWeather(): Flow<CurrentWeather> {
        return weatherDao.getCurrentWeather()
    }

    override suspend fun addCurrentWeather(currentWeather: CurrentWeather) {
        weatherDao.insertCurrentWeather(currentWeather)
    }

    override fun getAllAlarms():Flow<List<Alarm>>{
        return alarmDao.getAllAlarms()
    }

    override suspend fun addAlarm(alarm: Alarm):Long{
        return alarmDao.addAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm:Alarm):Int{
        return alarmDao.deleteAlarm(alarm)
    }

//    override suspend fun deleteAlarmById(alarmId:Int){
//        return alarmDao.deleteAlarmById(alarmId)
//    }

}


