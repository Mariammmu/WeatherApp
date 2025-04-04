package com.mariammuhammad.climate.Alert.view

import android.app.TimePickerDialog
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mariammuhammad.climate.Alert.viewmodel.AlertViewModel
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.model.pojo.Alarm
import com.mariammuhammad.climate.navigation.NavigationRoute
import com.mariammuhammad.climate.utiles.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAlarmScreen(
    alertViewModel: AlertViewModel
) {
    val alarmsState = alertViewModel.alarms.collectAsState()
    var showAddAlarmDialog by remember { mutableStateOf(false) }
    val alarmState = rememberAlarmCreationState()

    LaunchedEffect(Unit) {
        alertViewModel.getAlarms()
    }

    WeatherAlarmScaffold(
        alarmsState = alarmsState,
        showAddAlarmDialog = showAddAlarmDialog,
        alarmState = alarmState,
        onAddAlarmClick = { showAddAlarmDialog = true },
        onDismissDialog = { showAddAlarmDialog = false },
        onSaveAlarm = { alarm ->
            alertViewModel.addAlarm(alarm)
            showAddAlarmDialog = false
        },
        onDeleteAlarm = alertViewModel::deleteAlarm
    )
}

@Composable
private fun rememberAlarmCreationState() = remember {
    mutableStateOf(
        AlarmCreationState(
            startDate = null,
            startTime = null,
            endDate = null,
            endTime = null,
            notificationType = NotificationType.ALARM
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
    onSaveAlarm: (Alarm) -> Unit,
    onDeleteAlarm: (Alarm) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAlarmClick) {
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
                onSave = onSaveAlarm
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
        is Response.Loading -> CircularProgressIndicator(modifier)
        is Response.Success -> {
            if (state.data.isEmpty()) {
                EmptyAlarmsView(modifier)
            } else {
                AlarmListView(
                    alarms = state.data,
                    onDelete = onDeleteAlarm,
                    modifier = modifier
                )
            }
        }
        is Response.Failure -> {
            ErrorMessage(
                message = "Error loading alarms: ${state.error.localizedMessage}",
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAlarmDialog(
    state: AlarmCreationState,
    onDismiss: () -> Unit,
    onSave: (Alarm) -> Unit
) {
    val currentState = remember { mutableStateOf(state) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Weather Alert") },
        text = {
            AlarmCreationForm(
                state = currentState.value,
                onStateChange = { newState -> currentState.value = newState }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val alarm = createAlarmFromState(currentState.value)
                    onSave(alarm)
                },
                enabled = isAlarmStateValid(currentState.value)
            ) {
                Text("SAVE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        },
        containerColor = colorResource(R.color.background)
    )
}

@Composable
private fun AlarmCreationForm(
    state: AlarmCreationState,
    onStateChange: (AlarmCreationState) -> Unit
) {
    Column  {
        DatePickerRow(
            label = "Pick a date",
            selectedDate = state.startDate,
            onDateSelected = { onStateChange(state.copy(startDate = it)) }
        )

        Text("Start Time", style = MaterialTheme.typography.labelMedium)
        TimePickerRow(
            label = "Start time",
            selectedTime = state.startTime,
            onTimeSelected = { onStateChange(state.copy(startTime = it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // End Date/Time
        Text("End Time", style = MaterialTheme.typography.labelMedium)
//        DatePickerRow(
//            label = "End date",
//            selectedDate = state.endDate,
//            onDateSelected = { onStateChange(state.copy(endDate = it)) }
//        )
        TimePickerRow(
            label = "End time",
            selectedTime = state.endTime,
            onTimeSelected = { onStateChange(state.copy(endTime = it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Notification Type
        Text("Notify me by", style = MaterialTheme.typography.labelMedium)
        NotificationTypeSelector(
            selectedType = state.notificationType,
            onTypeSelected = { onStateChange(state.copy(notificationType = it)) }
        )
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
            painter = painterResource(R.drawable.google_calendar_icon), // Make sure you have this icon
            contentDescription = label,
            modifier = Modifier.padding(end = 8.dp),
        )
        Text(
            text = "$label: ${selectedDate?.let { dateFormatter.format(Date(it)) } ?: "Select date"}",
            modifier = Modifier.weight(1f)
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
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

    // Format to show time
    val formattedTime = selectedTime?.let {
        val hour = it.first
        val minute = it.second
        val amPm = if (it.third) "PM" else "AM"
        "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $amPm"
    } ?: "Select time"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.pending_clock_icon), // Ensure this icon exists
            contentDescription = label,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "$label: $formattedTime",
            modifier = Modifier.weight(1f)
        )
    }

    if (showTimePicker) {
        val hour = selectedTime?.first ?: currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = selectedTime?.second ?: currentTime.get(Calendar.MINUTE)

        // Open the TimePickerDialog
        TimePickerDialog(
            LocalContext.current,
            { _, selectedHour, selectedMinute ->
                // Handle AM/PM (based on 12-hour format)
                val isPM = selectedHour >= 12
                val hour12 = when {
                    selectedHour == 0 -> 12  // Handle midnight
                    selectedHour > 12 -> selectedHour - 12  // Convert to 12-hour format
                    else -> selectedHour
                }
                // Call onTimeSelected with the selected hour, minute, and AM/PM flag
                onTimeSelected(Triple(hour12, selectedMinute, isPM))
                showTimePicker = false  // Dismiss the time picker
            },
            hour,
            minute,
            false // Set to false for 12-hour format
        ).show()
    }
}

@Composable
@Preview
fun TimePickerRowPreview() {
    var selectedTime by remember { mutableStateOf<Triple<Int, Int, Boolean>?>(null) }

    TimePickerRow(
        label = "Alarm Time",
        selectedTime = selectedTime,
        onTimeSelected = { time ->
            selectedTime = time
        }
    )
}
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerRow(
    label: String,
    selectedTime: Triple<Int, Int, Boolean>?, // (hour, minute, isPM)
    onTimeSelected: (Triple<Int, Int, Boolean>) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()

    // Format to show time
    val formattedTime = selectedTime?.let {
        val hour = it.first
        val minute = it.second
        val amPm = if (it.third) "PM" else "AM"
        "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $amPm"
    } ?: "Select time"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.pending_clock_icon), // Ensure this icon exists
            contentDescription = label,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "$label: $formattedTime",
            modifier = Modifier.weight(1f)
        )
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedTime?.let { if (it.third) it.first + 12 else it.first }
                ?: currentTime.get(Calendar.HOUR_OF_DAY),
            initialMinute = selectedTime?.second ?: currentTime.get(Calendar.MINUTE),
            is24Hour = false // set to true for 24-hour format
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val isPM = timePickerState.hour >= 12
                        val hour12 = when {
                            timePickerState.hour == 0 -> 12
                            timePickerState.hour > 12 -> timePickerState.hour - 12
                            else -> timePickerState.hour
                        }
                        // Pass the selected time back with the 12-hour format and AM/PM indicator
                        onTimeSelected(Triple(hour12, timePickerState.minute, isPM))
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            TimePicker(
                state = timePickerState,
                modifier = Modifier.padding(16.dp),

            )
        }
    }
} */

@Composable
private fun NotificationTypeSelector(
    selectedType: NotificationType,
    onTypeSelected: (NotificationType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NotificationType.values().forEach { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                label = { Text(type.name) }
            )
        }
    }
}

@Composable
private fun EmptyAlarmsView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "You haven't any alarms",
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
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(alarms) { alarm ->
            AlarmItem(
                alarm = alarm,
                onDelete = { onDelete(alarm) }
            )
        }
    }
}

@Composable
private fun AlarmItem(
    alarm: Alarm,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${alarm.hour}:${alarm.minute.toString().padStart(2, '0')}",
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

            Text(
                text = "Location: (${alarm.lat.roundToDecimals(2)}, ${alarm.lon.roundToDecimals(2)})",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp))
        }
    }
}

private fun Double.roundToDecimals(decimalCount: Int): Double {
    val factor = 10.0.pow(decimalCount)
    return (this * factor).roundToInt() / factor
}
@Composable
private fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class AlarmCreationState(
    val startDate: Long?,
    val startTime: Triple<Int, Int, Boolean>?,
    val endDate: Long?,
    val endTime: Triple<Int, Int, Boolean>?,
    val notificationType: NotificationType
)

private fun createAlarmFromState(state: AlarmCreationState): Alarm {
    return Alarm(
        id = 0,
        cityName = "Selected Location",
        lat = 0.0,
        lon = 0.0,
        hour = state.startTime?.first ?: 0,
        minute = state.startTime?.second ?: 0
    )
}

private fun isAlarmStateValid(state: AlarmCreationState): Boolean {
    return state.startDate != null &&
            state.startTime != null &&
            state.endDate != null &&
            state.endTime != null
}

enum class NotificationType {
    ALARM, NOTIFICATION
}