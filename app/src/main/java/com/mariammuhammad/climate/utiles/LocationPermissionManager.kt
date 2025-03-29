package com.mariammuhammad.climate.utiles

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import android.provider.Settings


class LocationPermissionManager(
    private val context: Context,
//    private val permissionLauncher: ActivityResultLauncher<String>
) {
//https://developer.android.com/reference/androidx/activity/result/ActivityResultLauncher
    // https://developer.android.com/training/permissions/requesting

    //Provides precise location information. Required for accessing precise location data (GPS)
    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


//    //Provides less precise location information. Required for accessing approximate location data (Wi-Fi or network-based).
//    fun requestLocationPermission() {
//        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//    }


    //Like Mazin said before the user might deny because he doesn't know the reason for this permission so
    //showing a dialog explaining why the permission is needed and guiding users to settings if they denied it before.

//    fun shouldShowRationale(activity: Activity): Boolean {
//        return ActivityCompat.shouldShowRequestPermissionRationale(
//            activity,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    }
fun showLocationServiceRationaleDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Enable Location Services")
        .setMessage("We need access to your location to provide accurate weather information for your area. Please enable location services.")
        .setPositiveButton("Enable Location Services") { _, _ ->
            // Open location settings to enable location services
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
        .setNegativeButton("Cancel", null) // Dismiss the dialog if the user doesn't want to enable it
        .show()
}
}
