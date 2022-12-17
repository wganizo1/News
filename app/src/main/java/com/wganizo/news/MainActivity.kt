package com.wganizo.news

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wganizo.news.adapters.TopHeadlinesAdapter
import com.wganizo.news.contants.Constants
import com.wganizo.news.dialogs.ProgressDialog
import com.wganizo.news.viewmodels.TopHeadlinesViewModel
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val progressDialog = ProgressDialog()
    private val constants = Constants()
    private var client = OkHttpClient()
    private val topHeadlinesData = ArrayList<TopHeadlinesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
          getTopHeadlines("us")
    }
    private fun getTopHeadlines(country: String) {
        try {
            showLoading()
            val request = Request.Builder()
                .url("${constants.baseUrl}${constants.topHeadlines}?apiKey=${constants.apiKey}&country=$country")
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Error $e")
                    hideLoading()
                }
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body!!.string()

                    runOnUiThread {
                        val jsonObject = JSONTokener(responseData).nextValue() as JSONObject
                        val articlesArray = jsonObject.getString("articles")
                        val jsonArray = JSONTokener(articlesArray).nextValue() as JSONArray

                        val topHeadlinesRecyclerview =
                            findViewById<RecyclerView>(R.id.headline_recyclerview)
                        // Creating a vertical layout Manager
                        topHeadlinesRecyclerview.layoutManager =
                            LinearLayoutManager(applicationContext)
                        for (i in 0 until jsonArray.length()) {
                            val jsonData = jsonArray.getJSONObject(i)
                            println("Ganz ${jsonData.getString("title")}")
                            topHeadlinesData.add(
                                TopHeadlinesViewModel(
                                    jsonData.getString("title"),
                                    jsonData.getString("urlToImage"),
                                    jsonData.getString("author")
                                )
                            )
                        }
                        hideLoading()
                        //Passing my data to the Adapter
                        val topHeadlinesAdapter =
                            TopHeadlinesAdapter(topHeadlinesData, applicationContext)
                        // Setting the Adapter with the recyclerview
                        topHeadlinesRecyclerview.adapter = topHeadlinesAdapter
                    }
                }

            })
        }catch (e: Exception){
            println("Error $e")
            hideLoading()
        }
    }
    private fun showLoading(){
        progressDialog.show(this,getString(R.string.loading))
    }

    private fun hideLoading() {
        progressDialog.dialog.dismiss()
    }
}