package com.example.androidprotelcase.api

import com.example.androidprotelcase.BASE_URL
import com.example.androidprotelcase.model.NewsResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RetrofitAPI {
    companion object {
        fun create(): RetrofitAPI {
            val interceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RetrofitAPI::class.java)
        }
    }

    @GET("v2/everything")
    fun getNews(@Query("q") q: String? = null, @Query("sortBy") sortBy: String? = null, @Query("from") from: String? = null, @Query("to") to: String? = null, @Query("apiKey") apiKey: String? = null): Call<NewsResponse>? = null

}