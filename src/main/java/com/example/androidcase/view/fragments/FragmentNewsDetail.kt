package com.example.androidprotelcase.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.androidprotelcase.R
import kotlinx.android.synthetic.main.fragment_news_details.*

class FragmentNewsDetail : Fragment(){

    companion object{
        fun newInstance(): FragmentNewsDetail = FragmentNewsDetail()
        val options = RequestOptions()
            .error(R.drawable.ic_baseline_error_24)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.fragment_news_details, container, false)

        val detailToolbar = myView.findViewById<Toolbar>(R.id.detailToolbar)
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(detailToolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return myView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imageUrl = arguments!!.getString("imageUrl")
        val title = arguments!!.getString("title")
        val description = arguments!!.getString("description")
        val linkUrl = arguments!!.getString("url")
        val publishedAt = arguments!!.getString("publishedAt")

        Glide.with(context!!).load(imageUrl).apply(options).into(iwNewsDetail)
        twNewsDetail.text = title
        twNewsDetailDescription.text = description
        twNewsDetailURL.text = linkUrl
        twNewsDetailPublishedAt.text = publishedAt

        clMainNewsDetail.setOnClickListener {
            Log.d("MCC", "cl clicked.")
        }

        twNewsDetailURL.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                startActivity(intent)
            }catch (error: Exception){
                Log.e("MCC", "open url error: ${error.message}")
            }
        }
    }

}