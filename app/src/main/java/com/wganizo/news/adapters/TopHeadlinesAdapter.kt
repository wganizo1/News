package com.wganizo.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wganizo.news.R
import com.wganizo.news.viewmodels.TopHeadlinesViewModel

class TopHeadlinesAdapter(private val focustList: List<TopHeadlinesViewModel>, private val context: Context) : RecyclerView.Adapter<TopHeadlinesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_headlines_cardview, parent, false)
        return ViewHolder(view)
    }

    // Binding items to listview
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = focustList[position]

        // Setting the headline title
        holder.title.text = ItemsViewModel.title

        // Setting the news banner
        Glide.with(context)
            .load(ItemsViewModel.urlToImage)
            .into(holder.headlineBanner)

        // Show the Author
        holder.author.text = ItemsViewModel.author
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return focustList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val headlineBanner: ImageView = itemView.findViewById(R.id.headline_banner)
        val author: TextView = itemView.findViewById(R.id.author)
    }
}
