package com.example.technews.data.remote.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ArticlesJson (
    val source: ArticleSourceJson,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    @Json(name = "publishedAt")
    val date: String,
    val content: String
): Parcelable
