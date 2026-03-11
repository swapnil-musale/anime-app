package com.devx.data.local.mapper

import com.devx.data.local.entity.AnimeEntity
import com.devx.data.remote.dto.animeList.AnimeDto

fun AnimeDto.toEntity(): AnimeEntity {
    return AnimeEntity(
        malId = malId,
        title = title,
        titleEnglish = titleEnglish ?: title,
        episodes = episodes,
        score = score,
        imageUrl = images?.jpg?.largeImageUrl ?: images?.jpg?.imageUrl.orEmpty(),
        synopsis = synopsis
    )
}
