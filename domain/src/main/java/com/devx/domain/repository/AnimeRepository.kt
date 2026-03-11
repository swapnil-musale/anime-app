package com.devx.domain.repository

import com.devx.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun fetchTopAnime(page: Int): Result<Unit>
    fun observeAnimeList(): Flow<List<Anime>>
}
