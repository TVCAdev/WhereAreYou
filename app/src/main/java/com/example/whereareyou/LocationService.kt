package com.example.whereareyou

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LocationService : FirebaseMessagingService() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(){
        super.onCreate()
        Log.d("WhereAreYou", "LocationService is started")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("WhereAreYou", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("WhereAreYou", "Message data payload: ${remoteMessage.data}")

            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                Log.e("WhereAreYou", "Location permissions were luck.")
                return
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null) {
                    Log.d("WhereAreYou",
                        "Location latitude: ${location.latitude} longitude : ${location.longitude}")

                    sendDataToServer(this, postURL,
                        "{\"latitude\":\"${location.latitude}\",\"longitude\":\"${location.longitude}\"}")

                } else {
                    Log.e("WhereAreYou", "Location is not available...")
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d("WhereAreYou", "Refreshed token: $token")

        // send token
        sendDataToServer(this, postURL, "{\"token\":\"$token\"}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WhereAreYou", "LocationService is destroyed")
    }
}
