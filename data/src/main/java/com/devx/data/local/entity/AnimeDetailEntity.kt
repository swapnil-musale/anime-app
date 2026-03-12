package com.devx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_detail")
data class AnimeDetailEntity(
    @PrimaryKey
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String,
    val synopsis: String?,
    val genres: String,
    val rating: String?,
    val trailerEmbedUrl: String?,
)
