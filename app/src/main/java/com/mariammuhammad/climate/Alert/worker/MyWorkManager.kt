package com.mariammuhammad.climate.Alert.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mariammuhammad.climate.model.WeatherRepositoryImpl
import com.mariammuhammad.climate.model.local.WeatherDataBase
import com.mariammuhammad.climate.model.local.WeatherLocalDataSource
import com.mariammuhammad.climate.model.remote.RetrofitHelper
import com.mariammuhammad.climate.model.remote.WeatherRemoteDataSource
import com.mariammuhammad.climate.utiles.Constants
import kotlinx.coroutines.flow.last


// package >workers  alertworker : extend coroutine work
//repo  lat, long >> give me the details inside the work manager
//notification (custom )
//chanel

class MyWorkManager(
    private val context: Context,
    private val params: WorkerParameters,
) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result {
        val repo = WeatherRepositoryImpl(
            remoteDataSource = WeatherRemoteDataSource(RetrofitHelper.weatherService),
            localDataSource = WeatherLocalDataSource(
                favoritesDao = WeatherDataBase.getInstance(context).getFavoritesDao(),
                weatherDao = WeatherDataBase.getInstance(context).getWeatherDao(),
                alarmDao = WeatherDataBase.getInstance(context).getAlarmDao()
            )
        )

        val lat = inputData.getDouble(
            Constants.LATITUDE,
            defaultValue = 0.0
        )
        val lon = inputData.getDouble(
            Constants.LONGITUDE,
            defaultValue = 0.0
        )


        Log.i("TAG", "doWork: ${lat}/${lon}")
        val weatherDetails= repo.getWeatherForecast(lat,
            lon,
            units = Constants.UNITS_CELSIUS,
            lan = Constants.LANGUAGE_EN,
        ).last()



          NotificationHandler(context).createNotification(
              body = weatherDetails.weather.firstOrNull()?.description,
              title = "Climate"
          )
        return Result.success()
    }
}