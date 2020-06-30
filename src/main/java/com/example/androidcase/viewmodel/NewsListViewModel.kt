package com.example.androidprotelcase.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidprotelcase.API_KEY
import com.example.androidprotelcase.QUERY
import com.example.androidprotelcase.SORT_BY
import com.example.androidprotelcase.api.RetrofitAPI
import com.example.androidprotelcase.model.NewsDetails
import com.example.androidprotelcase.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsListViewModel: ViewModel(){

    var newsList = MutableLiveData<ArrayList<NewsDetails>>()
    var mFrom: String? = null
    var mTo: String? = null

    // RxJava-RxAndroid kullanabilirdim ancak ufak çaplı bir proje olduğu için kullanmadım.Retrofit'in orijinal fonksiyonlarını kullandım.
    fun getNewsData(){
        val service = RetrofitAPI.create()
        val call: Call<NewsResponse> = service.getNews(QUERY, SORT_BY, mFrom, mTo, API_KEY)!!

        call.enqueue(object : Callback<NewsResponse>{
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("MCC-NewsViewModel", "onFailure: ${t.message.toString()}")
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                try {
                    if (response.isSuccessful){
                        val data: ArrayList<NewsDetails> = response.body()!!.articles!!
                        newsList.postValue(data)
                    }
                    else{
                        Log.e("MCC-NewsViewModel", "response is unsuccessful.")
                    }
                }catch (error: Exception){
                    Log.e("MCC-NewsViewModel", "onResponse catch: ${error.message.toString()}")
                }
            }
        })
    }

}