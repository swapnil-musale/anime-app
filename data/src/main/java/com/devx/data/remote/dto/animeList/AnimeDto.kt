package com.devx.data.remote.dto.animeList

import androidx.annotation.Keep
import com.devx.domain.model.Anime
import com.devx.domain.util.Mapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AnimeDto(
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
) : Mapper<Anime> {
    override fun mapToDomain(): Anime {
        return Anime(
            malId = malId,
            title = title,
            titleEnglish = titleEnglish ?: title,
            episodes = episodes,
            score = score,
            imageUrl = images?.jpg?.largeImageUrl ?: images?.jpg?.imageUrl.orEmpty(),
            synopsis = synopsis,
        )
    }
}
