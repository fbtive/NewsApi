package com.example.technews.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticlesDao {
    @Insert
    suspend fun insert(article: ArticleModel)

    @Delete
    suspend fun delete(article: ArticleModel)

    @Query("SELECT * FROM table_article")
    fun getAll(): LiveData<List<ArticleModel>>

    @Query("DELETE FROM table_article")
    suspend fun clearAll()
}