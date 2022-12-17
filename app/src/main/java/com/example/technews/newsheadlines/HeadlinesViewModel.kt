package com.example.technews.newsheadlines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technews.data.HeadlinesRepository
import com.example.technews.data.domain.Article
import com.example.technews.data.domain.ResultData
import com.example.technews.data.domain.toDatabaseModel
import com.example.technews.data.local.ArticleModel
import com.example.technews.data.local.ArticlesDao
import com.example.technews.data.remote.response.asDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val repository: HeadlinesRepository
) : ViewModel() {

    var mainJob: Job? = null

    private val _shimmer = MutableLiveData<Boolean>(true)
    val shimmer: LiveData<Boolean>
        get() = _shimmer

    private val _remoteHeadlines = MutableLiveData<List<Article>>()
    private var _dbArticles = repository.getLocalArticles()
    val headlineList = MediatorLiveData<List<Article>>()

    init {
        refreshHeadlines()

        headlineList.addSource(_remoteHeadlines){
            it?.let{
                headlineList.value = compareRemoteAndLocal(it, _dbArticles.value)
            }
        }

        headlineList.addSource(_dbArticles) {
            it?.let {
                headlineList.value = compareRemoteAndLocal(_remoteHeadlines.value, it)
            }
        }
    }

    fun refreshHeadlines() {
        mainJob?.cancel()

        mainJob = viewModelScope.launch {
            val result = repository.getHeadline()
            if (result is ResultData.Success) {
                _remoteHeadlines.value = result.data.asDomainModel()
                _shimmer.value = false
            }
            if (result is ResultData.Error) {
                Log.d("ViewModel", result.exception.message!!)
            }
        }
    }

    fun saveArticleToDB(article: Article) {
        viewModelScope.launch {
            repository.saveArticle(article)
        }
    }

    private fun compareRemoteAndLocal(remote: List<Article>?, local: List<ArticleModel>?) : List<Article>? {
        var result = remote
        CoroutineScope(Dispatchers.Default).launch {
            remote?.let { list ->
                local?.let {
                    result = list.map { article ->
                        article.saved = local.any { model -> article.url == model.url }
                        article
                    }
                }
            }
        }
        return result
    }
}