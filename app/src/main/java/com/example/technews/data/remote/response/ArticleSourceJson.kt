package com.example.technews.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleSourceJson (
    val id: String,
    val name: String,
): Parcelable