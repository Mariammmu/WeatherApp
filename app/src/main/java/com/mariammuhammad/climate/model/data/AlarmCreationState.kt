package com.mariammuhammad.climate.model.data

data class AlarmCreationState(
    val startDate: Long?,
    val startTime: Triple<Int, Int, Boolean>?,
    val endTime: Triple<Int, Int, Boolean>?,
    //val notificationType: NotificationType
)