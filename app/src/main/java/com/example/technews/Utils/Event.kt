package com.example.technews.Utils

import androidx.lifecycle.Observer

class Event<out T>(private val content: T) {
    var handled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if(handled) {
            return null
        }else {
            handled = true
            return content
        }
    }
}

class EventObserver<T>(private val onEventUnhandled: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandled(it)
        }
    }
}