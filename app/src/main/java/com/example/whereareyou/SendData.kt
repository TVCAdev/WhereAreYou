package com.example.whereareyou

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SendData(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        Log.d("WhereAreYou", "Performing long running task in scheduled job")

        // get data from inputData
        val urldata = inputData.getString("urldata")
        val jsondata = inputData.getString("jsondata")

        if ((urldata == null) or (jsondata == null)) {
            Log.e("WhereAreYou", "urlata or jsondata were not set.")
            return Result.success()
        }
        Log.d("WhereAreYou", "jsondata: $jsondata urldata: $urldata.")

        // create HttpURLConnection
        val connection = URL(urldata).openConnection() as HttpsURLConnection
        try {
            // configure parameter
            connection.connectTimeout = 60000       // timeout before connection
            connection.readTimeout = 60000          // timeout for reading data
            connection.requestMethod = "POST"       // method
            connection.doOutput = true              // use body data
            connection.setChunkedStreamingMode(0)   // the body length is not known in advance
            connection.setRequestProperty("Content-type", "application/json; charset=utf-8")

            // write jsondata to body
            val outputStream = connection.outputStream
            outputStream.write(jsondata?.toByteArray())
            outputStream.flush()
            outputStream.close()

            // check response code
            val statusCode = connection.responseCode
            if (statusCode == HttpsURLConnection.HTTP_OK) {
                Log.d("WhereAreYou", "Sending data was succeed.")
            } else {
                Log.d("WhereAreYou", "Send Error statusCode: $statusCode.")
            }
        } catch (exception: Exception) {
            Log.e("WhereAreYou", exception.toString())
        } finally {
            connection.disconnect()
        }

        return Result.success()
    }
}