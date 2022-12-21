package com.example.technews.newsactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.technews.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber

//@HiltViewModel
class MainViewModel : ViewModel() {
    private val _eventLocaleChanged = MutableLiveData<Event<String>>()
    val eventLocaleChanged: LiveData<Event<String>>
        get() = _eventLocaleChanged

    fun setLocaleEvent(lang: String) {
        _eventLocaleChanged.value = Event(lang)
    }
}