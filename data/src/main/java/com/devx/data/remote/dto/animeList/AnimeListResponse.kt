package com.devx.data.remote.dto.animeList

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AnimeListResponse(
    @SerialName("pagination")
    val pagination: PaginationDto,
    @SerialName("data")
    val animeList: List<AnimeDto>,
)
