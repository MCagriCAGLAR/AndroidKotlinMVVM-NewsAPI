package com.example.androidprotelcase.view.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.androidprotelcase.R
import com.example.androidprotelcase.model.NewsDetails
import com.example.androidprotelcase.view.fragments.FragmentNewsDetail

class NewsListAdapter(private var context: Context, private val newsList: ArrayList<NewsDetails>): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>(){

    companion object{
        val options = RequestOptions()
            .error(R.drawable.ic_baseline_error_24)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.recycler_news_row, parent, false)
        return NewsViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Glide.with(context).load(newsList[position].urlToImage.toString()).apply(options).into(holder.iwNews)
        holder.twNews.text = newsList[position].title.toString()

        holder.iwNews.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("imageUrl", newsList[position].urlToImage.toString())
            bundle.putString("title", newsList[position].title.toString())
            bundle.putString("description", newsList[position].description.toString())
            bundle.putString("url", newsList[position].url.toString())
            bundle.putString("publishedAt", newsList[position].publishedAt.toString())
            val fragment = FragmentNewsDetail.newInstance()
            fragment.arguments = bundle
            val activity = it.context as AppCompatActivity?
            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.mainContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val iwNews = itemView.findViewById<ImageView>(R.id.iwNews)
        val twNews = itemView.findViewById<TextView>(R.id.twNews)
    }

}