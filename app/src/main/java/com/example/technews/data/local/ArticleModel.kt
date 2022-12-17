package com.example.technews.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.technews.data.domain.Article

@Entity(tableName = "table_article")
data class ArticleModel(
    @PrimaryKey
    val url: String,
    val author: String?,
    val title: String,
    val description: String?,
    val source: String?,
    val imageUrl: String?,
    val date: String,
    val content: String?
)

fun List<ArticleModel>.toDomainModel() : List<Article> {
    return map {
        Article(
            url = it.url,
            author = it.author,
            title = it.title,
            description = it.description,
            source = it.source,
            imageUrl = it.imageUrl,
            date = it.date,
            content = it.content
        )
    }
}