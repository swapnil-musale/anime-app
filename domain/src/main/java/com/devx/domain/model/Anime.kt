package com.devx.domain.model

data class Anime(
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String,
    val synopsis: String?
)
