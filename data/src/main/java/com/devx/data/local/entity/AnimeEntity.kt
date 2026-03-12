package com.devx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String,
    val synopsis: String?,
)
