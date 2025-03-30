package com.mariammuhammad.climate.utiles

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale


 object TimeAndDateFormatting {
    fun Long?.dayFormater(): String {
        return if (this != null) {
            val calendar = Calendar.getInstance(Locale.getDefault())

            calendar.timeInMillis = this * 1000

            DateFormat.format("EEEE", calendar).toString()

        } else ""
    }

    fun Long?.dateTimeFormater(): String {
        return if (this != null) {
            val calendar = Calendar.getInstance(Locale.getDefault())

            calendar.timeInMillis = this * 1000

            DateFormat.format("E, dd MMM yyyy | h:mm aa", calendar).toString()

        } else ""
    }


    fun Long?.timeFormater(): String {
        return if (this != null) {
            val calendar = Calendar.getInstance(Locale.getDefault())

            calendar.timeInMillis = this * 1000

            DateFormat.format("h:mm aa", calendar).toString()

        } else ""
    }

}