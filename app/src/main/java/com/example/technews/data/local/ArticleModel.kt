package com.example.technews.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

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