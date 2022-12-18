package com.wganizo.news.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.wganizo.news.R
import com.wganizo.news.viewmodels.TopHeadlinesViewModel


class Article{

    lateinit var dialog: CustomDialog
    private lateinit var bannerImage: ImageView
    private lateinit var headlineTitle: TextView
    private lateinit var content: TextView
    private lateinit var author: TextView
    private lateinit var readMore: Button

    fun show(context: Context, title: CharSequence?,position: Int, topHeadlinesData: ArrayList<TopHeadlinesViewModel>): Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.article, null)

        bannerImage = view.findViewById(R.id.headline_banner)
        headlineTitle = view.findViewById(R.id.title)
        content = view.findViewById(R.id.news_content)
        author = view.findViewById(R.id.author)
        readMore = view.findViewById(R.id.read_more)

        readMore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(topHeadlinesData[position].url))
            context.startActivity(intent)
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