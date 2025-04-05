package com.mariammuhammad.climate.utiles

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.health.connect.datatypes.ExerciseRoute
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import java.util.Locale

//The FusedLocationProvider usa higher-level API that simplifies the process of obtaining location data
// in Android, and it automatically selects the best available location source (GPS, Wi-Fi, etc.) based on
// accuracy and power consumption.
class LocationUpdate(private val context: Context) {


    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

//    private var lastLocation: Location? = null

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun promptEnableLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(onLocationAvail: (location: Location?) -> Unit) {
        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {  //request, callback, looper
                setPriority(Priority.PRIORITY_LOW_POWER) //GPS
            }.build(),

            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)

                    onLocationAvail.invoke(location.lastLocation)
                    fusedLocationClient.removeLocationUpdates(this)

                }
            },

            Looper.myLooper()
        )
    }

    fun getAddress(context: Context, latitude: Double, longitude: Double): String? {
        return try {
            Geocoder(context, Locale.getDefault()).getFromLocation(latitude, longitude, 1)
                ?.firstOrNull()?.subAdminArea //the full address
        } catch (e: Exception) {
            null
        }
    }
}