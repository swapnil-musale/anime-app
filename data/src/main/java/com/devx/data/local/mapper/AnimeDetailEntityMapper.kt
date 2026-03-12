package com.devx.data.local.mapper

import com.devx.data.local.entity.AnimeDetailEntity
import com.devx.data.remote.dto.animeDetail.AnimeDetailDto
import com.devx.domain.model.AnimeDetail

fun AnimeDetailDto.toEntity(): AnimeDetailEntity {
    return AnimeDetailEntity(
        malId = malId,
        title = title,
        titleEnglish = titleEnglish ?: title,
        episodes = episodes,
        score = score,
        imageUrl = images?.jpg?.largeImageUrl ?: images?.jpg?.imageUrl.orEmpty(),
        synopsis = synopsis,
        genres = genres?.joinToString(",") { it.name } ?: "",
        rating = rating,
        trailerEmbedUrl = trailer?.embedUrl
    )
}

fun AnimeDetailEntity.mapToDomain(): AnimeDetail {
    return AnimeDetail(
        malId = malId,
        title = title,
        titleEnglish = titleEnglish,
        episodes = episodes,
        score = score,
        imageUrl = imageUrl,
        synopsis = synopsis,
        genres = if (genres.isBlank()) emptyList() else genres.split(","),
        rating = rating,
        trailerEmbedUrl = trailerEmbedUrl
    )
}
