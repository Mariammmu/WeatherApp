package com.mariammuhammad.climate.Alert.worker

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.mariammuhammad.climate.R

class NotificationHandler(private val context: Context) {
    private val CHANNEL_ID = "weather_alerts"
    private val NOTIFICATION_ID = 123
    private val SNOOZE_REQUEST_CODE = 1001
    private val DISMISS_REQUEST_CODE = 1002

    val soundUri = Uri.parse("android.resource://${context.packageName}/raw/warning_alert")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    fun createNotification(body: String?, title: String?) {
        // Create notification channel
        createNotificationChannel()

        // Create pending intents for actions
        val snoozeIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "SNOOZE_ACTION"
            putExtra("notification_id", NOTIFICATION_ID)
        }

        val dismissIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "DISMISS_ACTION"
            putExtra("notification_id", NOTIFICATION_ID)
        }

        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            SNOOZE_REQUEST_CODE,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            DISMISS_REQUEST_CODE,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .addAction(
                R.drawable.snooze_zzz,
                "Snooze (5 min)",
                snoozePendingIntent
            )
            .addAction(
                R.drawable.cancel_icon,
                "Dismiss",
                dismissPendingIntent
            )
            .setAutoCancel(true)

        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        } else {
            showNotificationPermissionDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Weather Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Weather alert notifications"
            setSound(
                soundUri, AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            enableVibration(true)
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun hasNotificationPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun showNotificationPermissionDialog() {
        val builder = AlertDialog.Builder(context)
            .setTitle("Enable Notifications")
            .setMessage("To receive weather alerts, please enable notifications.")
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open the settings page for notifications
                openNotificationSettings()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // If the user cancels, keep asking (or take appropriate action)
                Toast.makeText(context, "Notifications are needed for alerts.", Toast.LENGTH_SHORT)
                    .show()
                showNotificationPermissionDialog() // Re-show the dialog
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
        context.startActivity(intent)
    }
}
/*
class NotificationHandler (private val context: Context){

    private val CHANNEL_ID = "123"

    val soundUri = Uri.parse("android.resource://${context.packageName}/raw/warning_alert")


    fun createNotification(body: String?, title: String?) {
        // Create notification channel if needed (Android 8+)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH // Make sure sound plays
            ).apply {
                description = "description"
                setSound(soundUri, AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build())
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

        // Check for POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(123, builder.build())
        } else {
            Log.w("NotificationHelper", "POST_NOTIFICATIONS permission not granted.")
            // Optionally trigger a permission request or handle accordingly
            showNotificationPermissionDialog()
        }
    }

     fun showNotificationPermissionDialog() {
        val builder = AlertDialog.Builder(context)
            .setTitle("Enable Notifications")
            .setMessage("To receive weather alerts, please enable notifications.")
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open the settings page for notifications
                openNotificationSettings()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // If the user cancels, keep asking (or take appropriate action)
                Toast.makeText(context, "Notifications are needed for alerts.", Toast.LENGTH_SHORT).show()
                showNotificationPermissionDialog() // Re-show the dialog
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
        context.startActivity(intent)
    }
}
 */