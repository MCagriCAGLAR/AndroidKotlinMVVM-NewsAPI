package com.example.androidprotelcase.model

data class NewsResponse(
    var status: String? = null,
    var articles: ArrayList<NewsDetails>? = null
)