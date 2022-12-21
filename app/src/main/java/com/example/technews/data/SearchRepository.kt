package com.example.technews.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.technews.R
import com.example.technews.data.domain.Article
import com.example.technews.data.domain.ResultData
import com.example.technews.data.remote.API.NewsApi
import com.example.technews.data.remote.response.ErrorJson
import com.example.technews.data.remote.response.NewsData
import com.example.technews.utils.LocalizationUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.IOException

class SearchRepository(
    val newsApi: NewsApi,
    val context: Context
) {
    val moshiAdapter = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val languageData : Map<String, String> = mapOf(
        LocalizationUtil.ENGLISH to "en",
        LocalizationUtil.INDONESIA to "id"
    )

    suspend fun searchArticles(query: String): ResultData<NewsData> {
        return withContext(Dispatchers.IO) {
            val pref = LocalizationUtil.getLanguage(context)
            val language = languageData[pref]

            try {
                val result = newsApi.searchArticles(query, language!!)
                if(result.isSuccessful) {
                    ResultData.Success(result.body()!!)
                }else {
                    val errorResponse = convertErrorBody(result.errorBody())
                    ResultData.Error(Exception(errorResponse?.message ?: context.getString(R.string.network_error)))
                }
            }catch (e: IOException) {
                ResultData.Error(Exception(context.getString(R.string.network_no_internet)))
            }catch (e: Exception) {
                ResultData.Error(Exception(context.getString(R.string.network_error)))
            }
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorJson? {
        return try {
            errorBody?.source()?.let {
                moshiAdapter.adapter(ErrorJson::class.java).fromJson(it)
            }
        }catch (e: Exception) {
            null
        }
    }
}