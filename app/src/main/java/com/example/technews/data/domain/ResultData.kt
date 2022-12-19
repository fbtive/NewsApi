package com.example.technews.data.domain

import com.example.technews.data.remote.response.NewsData
import okhttp3.ResponseBody

sealed class ResultData<out R>() {
    data class Success<out T>(val data: T): ResultData<T>()
    data class Error<T>(val exception: Exception): ResultData<T>()
    class Loading<T>: ResultData<T>()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success[data=$data"
            is Error -> "Error[error=$exception]"
            is Loading -> "Loading"
        }
    }
}