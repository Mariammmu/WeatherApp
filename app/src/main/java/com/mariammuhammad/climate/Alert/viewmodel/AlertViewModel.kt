package com.mariammuhammad.climate.Alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mariammuhammad.climate.model.WeatherRepository
import com.mariammuhammad.climate.model.data.Alarm
import com.mariammuhammad.climate.utiles.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AlertViewModel(private val repository: WeatherRepository) : ViewModel() {

    // Holds the state of alarms
    private val _alarms = MutableStateFlow<Response<List<Alarm>>>(Response.Loading)
    val alarms: StateFlow<Response<List<Alarm>>> = _alarms

    fun getAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getAlarms()
//                    .catch { exception ->
//                        _alarms.value = Response.Failure(exception)
//                    }
                    .collect { alarmList ->
                        _alarms.value = Response.Success(alarmList)
                    }
            } catch (e: Exception) {
                _alarms.value = Response.Failure(e)
            }
        }
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch {
            _alarms.value = Response.Loading
            try {
                repository.insertAlarm(alarm)

                val updatedList = repository.getAlarms().first()
                _alarms.value = Response.Success(updatedList)
            } catch (e: Exception) {
                _alarms.value = Response.Failure(e)
            }
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            try {
                repository.deleteAlarm(alarm)
                val currentAlarms = (_alarms.value as? Response.Success)?.data ?: emptyList()
                _alarms.value = Response.Success(currentAlarms - alarm)
            } catch (e: Exception) {
                _alarms.value = Response.Failure(e)
            }
        }
    }

//    fun deleteAlarmById(alarmId: Int) {
//        viewModelScope.launch {
//            try {
//                repository.deleteAlarmById(alarmId)
//                val currentAlarms = (_alarms.value as? Response.Success)?.data ?: emptyList()
//                _alarms.value = Response.Success(currentAlarms.filterNot { it.id == alarmId })
//            } catch (e: Exception) {
//                _alarms.value = Response.Failure(e)
//            }
//        }
//    }
}

class AlertViewModelFactory(
    private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            return AlertViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
