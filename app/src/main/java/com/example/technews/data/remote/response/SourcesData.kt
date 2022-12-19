package com.example.technews.data.remote.response

import android.os.Parcelable
import com.example.technews.data.domain.Sources
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SourcesData (
    val status: String,
    val sources: List<SourcesJson>
): Parcelable

fun SourcesData.asDomainModel() : List<Sources> {
    return sources.map {
        Sources(
            url = it.url,
            name = it.name,
            description = it.description,
            category = it.category,
            country = it.country
        )
    }
}