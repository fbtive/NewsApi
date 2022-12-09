package com.example.technews.data.remote.response

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SourcesData (
    val status: String,
    val sources: List<SourcesJson>
): Parcelable