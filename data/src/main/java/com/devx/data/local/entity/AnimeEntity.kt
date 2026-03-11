package com.devx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devx.domain.model.Anime
import com.devx.domain.util.Mapper

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String,
    val synopsis: String?,
) : Mapper<Anime> {
    override fun mapToDomain(): Anime {
        return Anime(
            malId = malId,
            title = title,
            titleEnglish = titleEnglish,
            episodes = episodes,
            score = score,
            imageUrl = imageUrl,
            synopsis = synopsis
        )
    }
}
