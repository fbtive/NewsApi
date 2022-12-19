package com.example.technews.data

import android.content.Context
import com.example.technews.R
import com.example.technews.data.domain.ResultData
import com.example.technews.data.remote.API.NewsApi
import com.example.technews.data.remote.response.ErrorJson
import com.example.technews.data.remote.response.SourcesData
import com.example.technews.utils.LocalizationUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.IOException

class SourcesRepository(
    val newsApi: NewsApi,
    val context: Context
) {

    val countryData : Map<String, String> = mapOf(
        LocalizationUtil.ENGLISH to "us",
        LocalizationUtil.INDONESIA to "id"
    )

    val moshiAdapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    suspend fun getSources() : ResultData<SourcesData> {
        return withContext(Dispatchers.IO) {
            val language = LocalizationUtil.getLanguage(context)
            val country = countryData[language]!!

            try {
                val response = newsApi.getSources(country)
                if(response.isSuccessful) {
                    ResultData.Success(response.body()!!)
                }else {
                    val errorJson = convertErrorBody(response.errorBody())
                    ResultData.Error(Exception(errorJson?.message))
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