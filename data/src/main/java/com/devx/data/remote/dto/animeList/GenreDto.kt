package com.devx.data.remote.dto.animeList

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GenreDto(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("name")
    val name: String,
)
