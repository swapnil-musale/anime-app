package com.devx.data.remote.dto.animeDetail

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AnimeDetailResponse(
    @SerialName("data")
    val animeDetail: AnimeDetailDto,
)
