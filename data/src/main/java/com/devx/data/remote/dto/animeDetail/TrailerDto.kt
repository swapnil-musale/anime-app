package com.devx.data.remote.dto.animeDetail

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TrailerDto(
    @SerialName("youtube_id")
    val youtubeId: String? = null,
    @SerialName("embed_url")
    val embedUrl: String? = null,
)
