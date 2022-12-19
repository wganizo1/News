package com.wganizo.news

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wganizo.news.adapters.TopHeadlinesAdapter
import com.wganizo.news.contants.Constants
import com.wganizo.news.dialogs.Article
import com.wganizo.news.dialogs.ProgressDialog
import com.wganizo.news.viewmodels.TopHeadlinesViewModel
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val progressDialog = ProgressDialog()
    private val article = Article()
    private val constants = Constants()
    private var client = OkHttpClient()
    private val topHeadlinesData = ArrayList<TopHeadlinesViewModel>()
    private var country = "us"
    private lateinit var favorites: ImageView
    private lateinit var home: ImageView
    private lateinit var refresh: ImageView
    private val storedFavourites = StoredFavourites()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Make API Call using the getTopHeadlines function
        getTopHeadlines(country)

        //For Options such as favorites, home and refresh
        onClick()
    }

    private fun getTopHeadlines(country: String) {
        try {
            //Show Loading dialog
            showLoading()
            val request = Request.Builder()
                .url("${constants.baseUrl}${constants.topHeadlines}?apiKey=${constants.apiKey}&country=$country")
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    //Error
                    println("Error $e")
                    hideLoading()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body!!.string()

                    //Display the headlines
                    runOnUiThread {
                        showHeadlines(responseData)
                    }
                }

            })
        } catch (e: Exception) {
            //Error
            println("Error $e")
            hideLoading()
        }
    }

    private fun showHeadlines(responseData: String) {
        topHeadlinesData.clear()
        // Parse json response
        val jsonObject = JSONTokener(responseData).nextValue() as JSONObject
        val articlesArray = jsonObject.getString("articles")
        val jsonArray = JSONTokener(articlesArray).nextValue() as JSONArray

        val topHeadlinesRecyclerview = findViewById<RecyclerView>(R.id.headline_recyclerview)
        // Creating a vertical layout Manager
        topHeadlinesRecyclerview.layoutManager = LinearLayoutManager(applicationContext)

        //Add Data
        for (i in 0 until jsonArray.length()) {
            val jsonData = jsonArray.getJSONObject(i)
            topHeadlinesData.add(
                TopHeadlinesViewModel(
                    jsonData.getString("title"),
                    jsonData.getString("urlToImage"),
                    jsonData.getString("author"),
                    jsonData.getString("content"),
                    jsonData.getString("url"),
                    jsonData.getString("publishedAt")
                )
            )
        }
        hideLoading()
        //Passing my data to the Adapter
        val topHeadlinesAdapter = TopHeadlinesAdapter(topHeadlinesData, applicationContext)
        // Setting the Adapter with the recyclerview
        topHeadlinesRecyclerview.adapter = topHeadlinesAdapter

        //recycler view click listener implement
        topHeadlinesRecyclerview.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                showArticle(position)
            }
        })

    }

    //on item click interface
    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

    private fun showLoading(){
        progressDialog.show(this,getString(R.string.loading))
    }

    private fun hideLoading() {
        progressDialog.dialog.dismiss()
    }

    private fun showArticle(position: Int){
        article.show(this,getString(R.string.loading),position,topHeadlinesData)
    }

    private fun onClick(){
        //Initialise
        favorites = findViewById(R.id.favourite)
        home = findViewById(R.id.home)
        refresh = findViewById(R.id.refresh)

        //Add Listeners
        refresh.setOnClickListener {
            getTopHeadlines(country)
        }
        home.setOnClickListener {
            getTopHeadlines(country)
        }
        favorites.setOnClickListener {
            val favoriteArticles = constants.articles + storedFavourites.readFavorites(applicationContext) + constants.terminateJson
            showHeadlines(favoriteArticles.replace("},]}","}]}"))
        }
    }

}
