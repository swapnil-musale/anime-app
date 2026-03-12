package com.devx.domain.repository

import com.devx.domain.model.Anime
import com.devx.domain.model.AnimeDetail
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun fetchTopAnime(page: Int): Result<Unit>
    fun observeAnimeList(): Flow<List<Anime>>

    suspend fun fetchAnimeDetail(animeId: Int): Result<AnimeDetail>
}
