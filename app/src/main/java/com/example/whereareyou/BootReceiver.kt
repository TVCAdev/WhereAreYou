package com.example.whereareyou

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class BootReceiver  : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("WhereAreYou", "Receive : ${intent.action}")

        // send token to server in case
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("WhereAreYou", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log
            Log.d("WhereAreYou", "Current token: $token")

            // send token
            sendDataToServer(context, BuildConfig.POST_URL, "{\"token\":\"$token\"}")
        })
    }
}