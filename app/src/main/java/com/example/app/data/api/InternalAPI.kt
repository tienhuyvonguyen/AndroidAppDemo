package com.example.app.data.api

import android.os.AsyncTask
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class InternalAPI : AsyncTask<Void?, Void?, Void?>() {
    var url: URL = URL("http://143.42.66.73:9090")
    protected override fun doInBackground(vararg params: Void?): Void? {
        return null
    }

    fun login(username: String?, password: String?) {
        try {
            val location = "/public/api/login.php"
            val url = URL(url.toString() + location)
            val connection = url.openConnection() as HttpsURLConnection
            connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (HTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36"
            )
            if (connection.responseCode == 200) {
                val `in` = connection.inputStream
                val response = `in`.bufferedReader().use { it.readText() }
                println(response)
            } else {
                println("Error")
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

}