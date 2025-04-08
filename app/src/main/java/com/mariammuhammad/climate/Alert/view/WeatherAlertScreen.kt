package com.mariammuhammad.climate.Alert.view

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mariammuhammad.climate.Alert.viewmodel.AlertViewModel
import com.mariammuhammad.climate.Alert.worker.MyWorkManager
import com.mariammuhammad.climate.Alert.worker.NotificationHandler
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.model.data.Alarm
import com.mariammuhammad.climate.model.data.AlarmCreationState
import com.mariammuhammad.climate.utiles.Constants
import com.mariammuhammad.climate.utiles.LocationUpdate
import com.mariammuhammad.climate.utiles.Response
import com.mariammuhammad.climate.view.MainActivity
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherAlarmScreen(
    alertViewModel: AlertViewModel,
) {
    val alarmsState = alertViewModel.alarms.collectAsState()
    var showAddAlarmDialog by remember { mutableStateOf(false) }
    val alarmState = rememberAlarmCreationState()

    val context = LocalContext.current
    val activity = LocalActivity.current as MainActivity



    LaunchedEffect(Unit) {
        alertViewModel.getAlarms()
    }

    WeatherAlarmScaffold(
        alarmsState = alarmsState,
        showAddAlarmDialog = showAddAlarmDialog,
        alarmState = alarmState,
        onAddAlarmClick = { showAddAlarmDialog = true },
        onDismissDialog = { showAddAlarmDialog = false },
        onSaveAlarm = { alarm, state ->
            showAddAlarmDialog = false

            val locationUpdate = LocationUpdate(context)

            Log.i("TAG", "WeatherAlarmScreen: locationUpdate")
            if(activity.myLat != null && activity.myLong!= null){
                val alarmTime= convertToUnixTime(
                    state.startDate ?: 0,
                    state.startTime?.first ?: 0,
                    state.startTime?.second ?: 0,
                    state.startTime?.third?: true
                )
                Log.i("TAG", "WeatherAlarmScreen:  Alarm time ${alarmTime}")

                val delay: Long =
                    alarmTime - Instant.now().epochSecond
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresCharging(false)
                    .build()


                val data = Data.Builder()
                    .putDouble(Constants.LATITUDE, activity.myLat!!)
                    .putDouble(Constants.LONGITUDE, activity.myLong!!)
                    .build()

                val finalAlarm = alarm.copy(
                    cityName = locationUpdate.getAddress(
                        context,
                        activity.myLat!!,
                        activity.myLong!!
                    ) ?: ""
                )

                alertViewModel.addAlarm(finalAlarm)

                Log.i("TAG", "WeatherAlarmScreen: ${delay}")

                val request = OneTimeWorkRequestBuilder<MyWorkManager>()
                    .setInitialDelay(delay, TimeUnit.SECONDS)
                    .setInputData(data)
                    .setConstraints(constraints)
                    .build()
                WorkManager.getInstance(context).enqueue(request)  //question: why enqueue

            }else{
                locationUpdate.getLastLocation { location ->
                    activity.myLat = location?.latitude
                    activity.myLong = location?.longitude

                    Log.i("TAG", "WeatherAlarmScreen: convert ${state.startDate}")
                    Log.i("TAG", "WeatherAlarmScreen: convert ${state.startTime?.first}")
                    Log.i("TAG", "WeatherAlarmScreen: convert ${state.startTime?.second}")
                    Log.i("TAG", "WeatherAlarmScreen: convert ${state.startTime?.third}")

                    Log.i("TAG", "WeatherAlarmScreen: ${TimeZone.getDefault().id}")
                    Log.i("TAG", "WeatherAlarmScreen: ${Locale.getDefault().country}")


                    val alarmTime= convertToUnixTime(
                        state.startDate ?: 0,
                        state.startTime?.first ?: 0,
                        state.startTime?.second ?: 0,
                        state.startTime?.third?: true
                    )
                    Log.i("TAG", "WeatherAlarmScreen:  Alarm time ${alarmTime}")

                    val delay: Long =
                        alarmTime - Instant.now().epochSecond
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresCharging(false)
                        .build()


                    val data = Data.Builder()
                        .putDouble(Constants.LATITUDE, location?.latitude ?: 0.0)
                        .putDouble(Constants.LONGITUDE, location?.longitude ?: 0.0)
                        .build()

                    val finalAlarm = alarm.copy(
                        cityName = locationUpdate.getAddress(
                            context,
                            location?.latitude ?: 0.0,
                            location?.longitude ?: 0.0
                        ) ?: ""
                    )

                    alertViewModel.addAlarm(finalAlarm)

                    Log.i("TAG", "WeatherAlarmScreen: ${delay}")

                    val request = OneTimeWorkRequestBuilder<MyWorkManager>()
                        .setInitialDelay(delay, TimeUnit.SECONDS)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .build()
                    WorkManager.getInstance(context).enqueue(request)  //question: why enqueue

                }
            }


        },

        onDeleteAlarm = alertViewModel::deleteAlarm
    )
}

/*fun convertToUnixTime(dateLong: Long, hour: Int, minute: Int, isPM: Boolean): Long {
    // Convert the long date to a Date object
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateLong

    // Set the hour and minute based on the 12-hour format
    val correctedHour = if (isPM) {
        // Adjust hour for PM (12 PM is 12, 1 PM is 13, etc.)
        if (hour == 12) 12 else hour + 12
    } else {
        // For AM, 12 AM is midnight (00)
        if (hour == 12) 0 else hour
    }

    // Set the hour and minute in the calendar
    calendar.set(Calendar.HOUR_OF_DAY, correctedHour)
    calendar.set(Calendar.MINUTE, minute)

    // Get the Unix timestamp in milliseconds
    return calendar.timeInMillis / 1000 // Convert to seconds
}
 */

fun convertToUnixTime(dateLong: Long, hour: Int, minute: Int, isPM: Boolean): Long {
    // Create a Calendar instance and set it to the date represented by dateLong
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateLong
    Log.d("```TAG```", "convertToUnixTime: ${isPM}")
    // Adjust the hour based on AM/PM
    var adjustedHour = hour
    if (isPM) {
        // Convert 12-hour format to 24-hour format for PM hours
        adjustedHour = if (hour == 12) 12 else hour + 12
    } else {
        // For AM, we need to handle the case where the hour is 12 (which is midnight in 24-hour format)
        if (hour == 12) adjustedHour = 0
    }
    Log.i("TAG", "convertToUnixTime: ${adjustedHour}")
    //Log.i("TAG", "convertToUnixTime: minute ${}")

    // Set the calendar's time to the specified hour and minute
    calendar.set(Calendar.HOUR_OF_DAY, adjustedHour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0) // Set seconds to 0
    calendar.set(Calendar.MILLISECOND, 0) // Set milliseconds to 0

    // Return the Unix timestamp (milliseconds since epoch)
    return calendar.timeInMillis/1000L
}
@Composable
private fun rememberAlarmCreationState() = remember {
    mutableStateOf(
        AlarmCreationState(
            startDate = null,
            startTime = null,
            endTime = null,
            //notificationType = NotificationType.ALARM
        )
    )
}

@Composable
private fun WeatherAlarmScaffold(
    alarmsState: State<Response<List<Alarm>>>,
    showAddAlarmDialog: Boolean,
    alarmState: State<AlarmCreationState>,
    onAddAlarmClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onSaveAlarm: (Alarm, AlarmCreationState) -> Unit,
    onDeleteAlarm: (Alarm) -> Unit
) {
    val context = LocalContext.current
    val notificationHandler = NotificationHandler(context)
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        onAddAlarmClick()

                    } else {

                        notificationHandler.showNotificationPermissionDialog()
                    }
                } else {
                    onAddAlarmClick()
                }

            }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Alarm")
            }
        }
    ) { padding ->
        WeatherAlarmContent(
            alarmsState = alarmsState,
            modifier = Modifier.padding(padding),
            onDeleteAlarm = onDeleteAlarm
        )

        if (showAddAlarmDialog) {
            AddAlarmDialog(
                state = alarmState.value,
                onDismiss = onDismissDialog,
                onSave = { alarm, state ->

                    onSaveAlarm(alarm, state)

                }
            )
        }
    }
}

@Composable
private fun WeatherAlarmContent(
    alarmsState: State<Response<List<Alarm>>>,
    modifier: Modifier = Modifier,
    onDeleteAlarm: (Alarm) -> Unit
) {
    when (val state = alarmsState.value) {
        is Response.Loading -> CircularProgressIndicator()
        is Response.Success -> {
            if (state.data.isEmpty()) {
                EmptyAlarmsView()
            } else {
                AlarmListView(
                    alarms = state.data,
                    onDelete = onDeleteAlarm,
                )
            }
        }

        is Response.Failure -> {
            Log.i("TAG", "WeatherAlarmContent: ${state.error}")
            ErrorMessage(
                message = "Error loading alarms: ${state.error.localizedMessage}",
            )
        }
    }
}

@Composable
private fun AddAlarmDialog(
    state: AlarmCreationState,
    onDismiss: () -> Unit,
    onSave: (Alarm, AlarmCreationState) -> Unit
) {
    val currentState = remember { mutableStateOf(state) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(R.string.create_weather_alert),
                color = colorResource(R.color.background),
                fontFamily = FontFamily(Font(R.font.alfa_slab))

            )
        },
        text = {
            AlarmCreationForm(
                state = currentState.value,
                onStateChange = { newState -> currentState.value = newState }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    Log.d("```TAG```", "AddAlarmDialog: ${currentState.value.startTime?.third}")
                    val alarm = createAlarmFromState(currentState.value)
                    onSave(alarm, currentState.value)
                },
                enabled = isAlarmStateValid(currentState.value)
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        containerColor = colorResource(R.color.off_white),
        textContentColor = colorResource(R.color.background)

    )
}

@Composable
private fun AlarmCreationForm(
    state: AlarmCreationState,
    onStateChange: (AlarmCreationState) -> Unit
) {
    Column {
        Text(stringResource(R.string.pick_a_date), fontFamily = FontFamily(Font(R.font.alfa_slab)))

        DatePickerRow(
            label = "",
            selectedDate = state.startDate,
            onDateSelected = { onStateChange(state.copy(startDate = it)) }
        )

        Text(stringResource(R.string.start_time), fontFamily = FontFamily(Font(R.font.alfa_slab)))
        TimePickerRow(
            label = stringResource(R.string.start_time),
            selectedTime = state.startTime,
            onTimeSelected = { onStateChange(state.copy(startTime = it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // End Date/Time
        Text(stringResource(R.string.end_time), fontFamily = FontFamily(Font(R.font.alfa_slab)))
        TimePickerRow(
            label = stringResource(R.string.end_time),
            selectedTime = state.endTime,
            onTimeSelected = { onStateChange(state.copy(endTime = it)) }
        )

//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Notify me by", fontFamily = FontFamily(Font(R.font.alfa_slab)))
//        NotificationTypeSelector(
//            selectedType = state.notificationType,
//            onTypeSelected = { onStateChange(state.copy(notificationType = it)) }
//        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerRow(
    label: String,
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val currentDate = Calendar.getInstance().timeInMillis
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.google_calendar_icon),
            contentDescription = label,
            modifier = Modifier.padding(end = 8.dp),
            tint = Color.Unspecified
        )
        Text(
            text = "$label ${
                selectedDate?.let { dateFormatter.format(Date(it)) } ?: stringResource(
                    R.string.select_a_date
                )
            }",
            modifier = Modifier.weight(1f),
            color = colorResource(R.color.background),
            fontFamily = FontFamily(Font(R.font.alfa_slab))
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate ?: currentDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    // Only allow today and future dates
                    return utcTimeMillis >= currentDate - (24 * 60 * 60 * 1000)
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(it)
                            showDatePicker = false
                        }
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun TimePickerRow(
    label: String,
    selectedTime: Triple<Int, Int, Boolean>?, // (hour, minute, isPM)
    onTimeSelected: (Triple<Int, Int, Boolean>) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()

    val formattedTime = selectedTime?.let {
        val hour = it.first
        val minute = it.second
        val amPm = if (it.third) "PM" else "AM"
        "${hour.toString().padStart(2, '0')} : ${minute.toString().padStart(2, '0')} $amPm"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.pending_clock_icon),
            contentDescription = label,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "$label: ${formattedTime ?: stringResource(R.string.select_time)}",
            modifier = Modifier.weight(1f),
            color = colorResource(R.color.background),
            fontFamily = FontFamily(Font(R.font.alfa_slab))
        )
    }

    if (showTimePicker) {
        val hour = selectedTime?.first ?: currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = selectedTime?.second ?: currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            LocalContext.current,
            { _, selectedHour, selectedMinute ->
                // handle AM/PM (based on 12-hour format)
                val isPM = selectedHour >= 12
                val hour12 = when {
                    selectedHour == 0 -> 12  // Handle midnight
                    selectedHour > 12 -> selectedHour - 12  // Convert to 12-hour format
                    else -> selectedHour
                }

                onTimeSelected(Triple(hour12, selectedMinute, isPM))
                showTimePicker = false
            },
            hour,
            minute,
            false
        )

        timePickerDialog.setOnDismissListener {
            showTimePicker = false
        }

        timePickerDialog.setOnCancelListener {
            showTimePicker = false
        }

        timePickerDialog.show()
    }
}

//@Composable
//private fun NotificationTypeSelector(
//    selectedType: NotificationType,
//    onTypeSelected: (NotificationType) -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceEvenly
//    ) {
//        NotificationType.values().forEach { type ->
//            FilterChip(
//                selected = selectedType == type,
//                onClick = { onTypeSelected(type) },
//                label = { Text(type.name) }
//            )
//        }
//    }
//}

@Composable
private fun EmptyAlarmsView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.empty_alarms),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            color = colorResource(R.color.off_white),
            fontFamily = FontFamily(Font(R.font.alfa_slab))
        )
    }
}

@Composable
private fun AlarmListView(
    alarms: List<Alarm>,
    onDelete: (Alarm) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background)),
        //verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn { //modifier = Modifier.background(colorResource(R.color.background))) {
            items(alarms) { alarm ->
                AlarmItem(
                    alarm = alarm,
                    onDelete = { onDelete(alarm) }
                )
            }
        }
    }
}

@Composable
private fun AlarmItem(
    alarm: Alarm,
    onDelete: () -> Unit
) {
    val hourIn12HourFormat = if (alarm.hour % 12 == 0) 12 else alarm.hour % 12
    val amOrPm = if (alarm.hour < 12) "AM" else "PM"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${hourIn12HourFormat}:${
                        alarm.minute.toString().padStart(2, '0')
                    } $amOrPm",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Alarm")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = alarm.cityName,
                style = MaterialTheme.typography.bodyLarge
            )
//            Text(
//                text = "Location: ",    //(${alarm.lat}, ${alarm.lon})",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(top = 4.dp)
//            )
        }
    }
}


@Composable
private fun ErrorMessage(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Warning, contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp)
            )

            Text(
                text = message, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center
            )
        }
    }
}

private fun createAlarmFromState(state: AlarmCreationState): Alarm {
    return Alarm(
        cityName = "Selected Location",
//        lat = ,
//        lon = 0.0,
        hour = state.startTime?.first ?: 0,
        minute = state.startTime?.second ?: 0
    )
}

private fun isAlarmStateValid(state: AlarmCreationState): Boolean {
    return state.startDate != null &&
            state.startTime != null &&
            state.endTime != null
}

//enum class NotificationType {
//    ALARM, NOTIFICATION
//}