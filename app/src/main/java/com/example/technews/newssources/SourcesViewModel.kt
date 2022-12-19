package com.example.technews.newssources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technews.data.SourcesRepository
import com.example.technews.data.domain.ResultData
import com.example.technews.data.domain.Sources
import com.example.technews.data.remote.response.asDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    private val repository: SourcesRepository
) : ViewModel() {

    private var job: Job? = null

    private val _shimmer = MutableLiveData<Boolean>(true)
    val shimmer: LiveData<Boolean>
        get() = _shimmer

    private val _isRefreshing = MutableLiveData<Boolean>(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _sources = MutableLiveData<List<Sources>>()
    val sources: LiveData<List<Sources>>
        get() = _sources

    init {
        refreshSources()
    }

    private fun refreshSources() {
        job?.cancel()

        job = viewModelScope.launch {
            val result = repository.getSources()

            if(result is ResultData.Success) {
                _sources.value = result.data.asDomainModel()
                _shimmer.value = false
            }
            if(result is ResultData.Error) {
                Timber.d(result.exception.message)
            }

            _isRefreshing.value = false
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        refreshSources()
    }
}