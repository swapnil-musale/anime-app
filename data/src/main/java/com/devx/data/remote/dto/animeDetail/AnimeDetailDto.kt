package com.devx.data.remote.dto.animeDetail

import androidx.annotation.Keep
import com.devx.data.remote.dto.animeList.GenreDto
import com.devx.data.remote.dto.animeList.ImagesDto
import com.devx.domain.model.AnimeDetail
import com.devx.domain.util.Mapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AnimeDetailDto(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("title_english")
    val titleEnglish: String? = null,
    @SerialName("episodes")
    val episodes: Int? = null,
    @SerialName("score")
    val score: Double? = null,
    @SerialName("images")
    val images: ImagesDto? = null,
    @SerialName("synopsis")
    val synopsis: String? = null,
    @SerialName("genres")
    val genres: List<GenreDto>? = null,
    @SerialName("rating")
    val rating: String? = null,
    @SerialName("trailer")
    val trailer: TrailerDto? = null,
) : Mapper<AnimeDetail> {
    override fun mapToDomain(): AnimeDetail {
        return AnimeDetail(
            malId = malId,
            title = title,
            titleEnglish = titleEnglish ?: title,
            episodes = episodes,
            score = score,
            imageUrl = images?.jpg?.largeImageUrl ?: images?.jpg?.imageUrl.orEmpty(),
            synopsis = synopsis,
            genres = genres?.map { it.name } ?: emptyList(),
            rating = rating,
            trailerEmbedUrl = trailer?.embedUrl
        )
    }
}
