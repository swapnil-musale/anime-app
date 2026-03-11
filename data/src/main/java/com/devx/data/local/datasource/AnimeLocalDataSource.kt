package com.devx.data.local.datasource

import com.devx.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

interface AnimeLocalDataSource {
    fun observeAnimeList(): Flow<List<AnimeEntity>>
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)
    suspend fun clearAnimeList()
}
