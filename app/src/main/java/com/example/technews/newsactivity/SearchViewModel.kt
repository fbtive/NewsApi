package com.example.technews.newsactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technews.data.SearchRepository
import com.example.technews.data.domain.Article
import com.example.technews.data.domain.ResultData
import com.example.technews.data.remote.response.asDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val repository: SearchRepository): ViewModel() {

    private var job: Job? = null

    private val _isRefreshing = MutableLiveData<Boolean>(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>>
        get() = _articles

    fun searchArticle(query: String) {
        job?.cancel()

        job = viewModelScope.launch {
            _isRefreshing.value = true
            val result = repository.searchArticles(query)
            if(result is ResultData.Success) {
                _articles.value = result.data.asDomainModel()
            }
            if(result is ResultData.Error) {
                Timber.d("Error Search, ${result.exception.message}")
            }
        }
        _isRefreshing.value = false
    }
}