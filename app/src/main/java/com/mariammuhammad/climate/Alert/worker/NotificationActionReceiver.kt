package com.mariammuhammad.climate.Alert.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            "SNOOZE_ACTION" -> {
                val notificationId = intent.getIntExtra("notification_id", 0)
                cancelNotification(context, notificationId)
                scheduleSnoozedNotification(context)
            }
            "DISMISS_ACTION" -> {
                val notificationId = intent.getIntExtra("notification_id", 0)
                cancelNotification(context, notificationId)
            }
        }
    }

    private fun cancelNotification(context: Context, notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun scheduleSnoozedNotification(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<MyWorkManager>()
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}