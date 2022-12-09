package com.example.technews.data.remote.API

import com.example.technews.BuildConfig
import com.example.technews.data.remote.response.NewsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = BuildConfig.api_key
    }

    @Headers("X-Api-Key", API_KEY)
    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("category") category: String,
        @Query("country") country: String
    ): Response<NewsData>
}