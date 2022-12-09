package com.example.technews.data.remote.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class NewsData(
    val status: String,
    @Json(name = "totalResults")
    val total: Int,
    val articles: List<ArticlesJson>
): Parcelable