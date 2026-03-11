package com.devx.data.remote.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ImagesDto(
    @SerialName("jpg")
    val jpg: ImageUrlDto? = null,
)

@Keep
@Serializable
data class ImageUrlDto(
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("large_image_url")
    val largeImageUrl: String? = null,
)
