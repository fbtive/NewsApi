package com.example.technews.data.remote.response

import android.os.Parcelable
import com.example.technews.data.domain.Article
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

fun NewsData.asDomainModel(): List<Article> {
    return articles.map {
        Article(
            source = it.source.name,
            author = it.author,
            title = it.title,
            description = it.description,
            url = it.url,
            imageUrl = it.urlToImage,
            date = it.date,
            content = it.content
        )
    }
}