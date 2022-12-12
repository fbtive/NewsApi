package com.example.technews.data.domain

import android.os.Parcelable
import com.example.technews.utils.formatIsoDateToPattern
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val source: String?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val date: String,
    val content: String?
): Parcelable {

    val shortDescription: String?
        get() = description?.let {
            it.substring(0, Math.min(200, description.length)) + ".."
        } ?: ""

    val dateFormat: String
        get() = formatIsoDateToPattern(date, "yyyy MMM, dd")
}