package com.devx.data.local.datasource

import com.devx.data.local.entity.AnimeDetailEntity
import com.devx.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

interface AnimeLocalDataSource {
    fun observeAnimeList(): Flow<List<AnimeEntity>>
    suspend fun getAnimeList(): List<AnimeEntity>
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)
    suspend fun replaceAnimeList(animeList: List<AnimeEntity>)
    suspend fun clearAnimeList()

    suspend fun insertAnimeDetail(animeDetail: AnimeDetailEntity)
    suspend fun getAnimeDetail(animeId: Int): AnimeDetailEntity?
}
