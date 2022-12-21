package com.example.technews.data.domain

import android.os.Parcelable
import com.example.technews.data.local.ArticleModel
import com.example.technews.utils.formatIsoDateToPattern
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val source: String?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val date: String,
    val content: String?,
    var saved: Boolean = false
): Parcelable {

    val shortTitle: String
        get() = title.substring(0, Math.min(100, title.length)) + ".."

    val shortDescription: String?
        get() = description?.let {
            it.substring(0, Math.min(200, description.length)) + ".."
        } ?: ""

    val dateFormat: String
        get() = formatIsoDateToPattern(date, "yyyy MMM, dd")
}

fun Article.toDatabaseModel(): ArticleModel {
    return ArticleModel(
        url = this.url,
        author = this.author,
        title = this.title,
        description = this.description,
        source = this.source,
        imageUrl = this.imageUrl,
        date = this.date,
        content = this.content
    )
}