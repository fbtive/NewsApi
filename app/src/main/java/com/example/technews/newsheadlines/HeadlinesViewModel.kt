package com.example.technews.newsheadlines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technews.data.HeadlinesRepository
import com.example.technews.data.domain.Article
import com.example.technews.data.domain.ResultData
import com.example.technews.data.remote.response.asDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val repository: HeadlinesRepository
) : ViewModel() {

    var job: Job? = null

    private val _headlineList = MutableLiveData<List<Article>>()
    val headlineList: LiveData<List<Article>>
        get() = _headlineList

    private val _headlineShimmer = MutableLiveData<Boolean>(true)
    val headlineShimmer: LiveData<Boolean>
        get() = _headlineShimmer

    init {
        refreshHeadlines()
    }

    fun refreshHeadlines() {
        job?.cancel()

        job = viewModelScope.launch {
            val result = repository.getHeadline()
            if (result is ResultData.Success) {
                _headlineList.value = result.data.asDomainModel()
            }
            if (result is ResultData.Error) {
                Log.d("ViewModel", result.exception.message!!)
            }
        }
    }
}