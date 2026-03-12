package com.devx.domain.model

data class AnimeDetail(
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String,
    val synopsis: String?,
    val genres: List<String>,
    val rating: String?,
    val trailerEmbedUrl: String?
)
