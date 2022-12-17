package com.example.technews.newsbookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technews.data.domain.Article
import com.example.technews.data.domain.toDatabaseModel
import com.example.technews.data.local.ArticleModel
import com.example.technews.data.local.ArticlesDao
import com.example.technews.data.local.toDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(val articlesDao: ArticlesDao): ViewModel() {

    private val _articles = articlesDao.getAll()
    val articles = Transformations.map(_articles) {
        it.toDomainModel()
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val model = article.toDatabaseModel()
                    articlesDao.delete(model)
                }catch (e: Exception) {
                    Timber.e("Error [DELETE], ${e.message}")
                }
            }
        }
    }
}