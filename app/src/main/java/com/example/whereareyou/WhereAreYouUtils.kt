package com.example.whereareyou

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf

fun sendDataToServer(targetContext:Context, urldata: String, jsondata: String) {
    Log.d("WhereAreYou", "Send $jsondata to $urldata.")

    // set data for worker
    val inputData: Data = workDataOf("urldata" to urldata, "jsondata" to jsondata)
    val work = OneTimeWorkRequest.Builder(SendData::class.java)
        .setInputData(inputData)
        .build()
    WorkManager.getInstance(targetContext).beginWith(work).enqueue()
}
