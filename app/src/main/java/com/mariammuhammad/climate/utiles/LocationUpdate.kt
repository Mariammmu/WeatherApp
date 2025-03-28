package com.mariammuhammad.climate.utiles

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
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
import com.mariammuhammad.climate.model.pojo.Coord

//The FusedLocationProvider usa higher-level API that simplifies the process of obtaining location data
// in Android, and it automatically selects the best available location source (GPS, Wi-Fi, etc.) based on
// accuracy and power consumption.
    class LocationUpdate(private val context: Context) {

        private val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        private lateinit var locationCallback: LocationCallback
        private var lastLocation: Location? = null


        //this will keep the value of our current location
        var currentLocation = mutableStateOf(Coord(0.0, 0.0))

        //    var locationState = mutableStateOf<LocationState>(LocationState.Loading)

        init {
            setupLocationCallback()
        }

        private fun setupLocationCallback() {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.locations.firstOrNull()

                    if (location != null && (lastLocation == null || lastLocation!!.distanceTo(location) >= 300)) {
                        lastLocation = location
                        currentLocation.value = Coord(location.latitude, location.longitude)
                        //30.0444° N, 31.2357°

                        Log.d("Location", "Updated Location: $location")
                    }
                }
            }
        }

            fun promptEnableLocationSettings() {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }


            @SuppressLint("MissingPermission")
            fun startLocationUpdates() {
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    10000
                ) // 10-second interval
                    .setMinUpdateDistanceMeters(300f) // Only update location if user moves 300 meters
                    .build()

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }

            fun checkIfLocationEnabled() {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
                if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    promptEnableLocationSettings()
                } else {
                    startLocationUpdates()
                }
            }


        // Function to stop location updates
        fun stopLocationUpdates() {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
}

//    @SuppressLint("MissingPermission")
//    /*
//    The Task API is designed to handle operations that take time, like network requests, file I/O, or obtaining a device’s location. When you invoke certain location-related methods (like fusedLocationClient.lastLocation), they return a Task<Location> because the operation (fetching the location) is asynchronous. The result will be available at some point in the future, but not immediately.
//     */
//    suspend fun getLastKnownLocation(): Task<Location>? {
//        try {
//            val location = fusedLocationClient.lastLocation
//            return location
//        } catch (e: Exception) {
//            // Handle errors such as permissions or location unavailability
//            return null
//        }
//    }
//


