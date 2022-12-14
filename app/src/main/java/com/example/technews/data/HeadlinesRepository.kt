package com.example.technews.data

import android.content.Context
import android.util.Log
import com.example.technews.R
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
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class HeadlinesRepository constructor(
    private val newsApi: NewsApi,
    private val context: Context
){

    val moshiAdapter = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val countryData : Map<String, String> = mapOf(
        LocalizationUtil.ENGLISH to "us",
        LocalizationUtil.INDONESIA to "id"
    )

    suspend fun getHeadline(): ResultData<NewsData> {
        return withContext(Dispatchers.IO) {
            val language = LocalizationUtil.getLanguage(context)
            var country = countryData.get(language)!!

            try {
                val response = newsApi.getHeadlines("", country)

                if (response.isSuccessful) {
                    ResultData.Success(data = response.body()!!)
                } else {
                    val errorResponse: ErrorJson? = convertErrorBody(response.errorBody())
                    ResultData.Error(Exception(errorResponse?.message ?: context.getString(R.string.network_error)))
                }
            } catch (e: IOException) {
                ResultData.Error(Exception(context.getString(R.string.network_no_internet)))
            } catch (e: Exception) {
                ResultData.Error(Exception(context.getString(R.string.network_error)))
            }
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorJson? {
        return try {
            errorBody?.source()?.let {
                moshiAdapter.adapter(ErrorJson::class.java).fromJson(it)
            }
        } catch (e: Exception) {
            null
        }
    }
}