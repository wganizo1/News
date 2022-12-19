package com.wganizo.news.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.wganizo.news.R
import com.wganizo.news.StoredFavourites
import com.wganizo.news.viewmodels.TopHeadlinesViewModel
import org.json.JSONException
import org.json.JSONObject


class Article{

    lateinit var dialog: CustomDialog
    private lateinit var bannerImage: ImageView
    private lateinit var headlineTitle: TextView
    private lateinit var content: TextView
    private lateinit var author: TextView
    private lateinit var readMore: Button
    private lateinit var saveArticle: Button
    private val storedFavourites = StoredFavourites()

    fun show(context: Context, title: CharSequence?,position: Int, topHeadlinesData: ArrayList<TopHeadlinesViewModel>): Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.article, null)

        bannerImage = view.findViewById(R.id.headline_banner)
        headlineTitle = view.findViewById(R.id.title)
        content = view.findViewById(R.id.news_content)
        author = view.findViewById(R.id.author)
        readMore = view.findViewById(R.id.read_more)
        saveArticle = view.findViewById(R.id.save_article)

        readMore.setOnClickListener {
            //Open Url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(topHeadlinesData[position].url))
            context.startActivity(intent)
        }
        saveArticle.setOnClickListener {
            //Save current article
            val json = JSONObject()

            try {
                json.put("title", topHeadlinesData[position].title)
                json.put("author", topHeadlinesData[position].author)
                json.put("content", topHeadlinesData[position].content)
                json.put("url", topHeadlinesData[position].url)
                json.put("urlToImage", topHeadlinesData[position].urlToImage)
                json.put("publishedAt", topHeadlinesData[position].publishedAt)
                } catch (e: JSONException) {
                e.printStackTrace()
            }
            storedFavourites.writeToFile("${json},${storedFavourites.readFavorites(context)}"  ,context)
            saveArticle.isVisible = false
            Toast.makeText(context,"Saved", Toast.LENGTH_LONG).show()
        }

        headlineTitle.text = topHeadlinesData[position].title
        content.text = topHeadlinesData[position].content
        author.text = topHeadlinesData[position].author
        // Setting the news banner
        Glide.with(context)
            .load(topHeadlinesData[position].urlToImage)
            .into(bannerImage)

        dialog = CustomDialog(context)
        dialog.setContentView(view)
        dialog.show()
        return dialog
    }

    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(R.color.dialogBackground)
            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                insets.consumeSystemWindowInsets()
            }
        }
    }


}