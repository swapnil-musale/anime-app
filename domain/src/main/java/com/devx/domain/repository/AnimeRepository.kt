package com.devx.domain.repository

import com.devx.domain.model.Anime
import com.devx.domain.model.AnimeDetail
import com.devx.domain.model.PaginationInfo
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun fetchAnimePage(isRefresh: Boolean): Result<PaginationInfo>
    fun observeAnimeList(): Flow<List<Anime>>

    suspend fun fetchAnimeDetail(animeId: Int): Result<AnimeDetail>
}
