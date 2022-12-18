package com.wganizo.news

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class StoredFavourites{
    fun writeToFile(data: String, context: Context) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput("favorites.txt", AppCompatActivity.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: Exception) {
            println("Error!! $e")
        }
    }

    fun readFavorites(context: Context): String {
        var ret = ""
        try {
            val inputStream: InputStream? = context.openFileInput("favorites.txt")
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String?
                val stringBuilder = StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append("\n").append(receiveString)
                }
                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
        } catch (e: Exception) {
        }
        return ret
    }
}
