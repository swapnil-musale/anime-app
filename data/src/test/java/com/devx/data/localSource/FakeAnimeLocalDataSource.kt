package com.devx.data.localSource

import com.devx.data.local.datasource.AnimeLocalDataSource
import com.devx.data.local.entity.AnimeDetailEntity
import com.devx.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAnimeLocalDataSource : AnimeLocalDataSource {
    var storedList: List<AnimeEntity> = emptyList()
    var insertedList: List<AnimeEntity> = emptyList()
    var replacedList: List<AnimeEntity> = emptyList()

    var cachedDetail: AnimeDetailEntity? = null
    var insertedDetail: AnimeDetailEntity? = null

    override fun observeAnimeList(): Flow<List<AnimeEntity>> {
        return flowOf(storedList)
    }

    override suspend fun getAnimeList(): List<AnimeEntity> {
        return storedList
    }

    override suspend fun insertAnimeList(animeList: List<AnimeEntity>) {
        insertedList = animeList
    }

    override suspend fun replaceAnimeList(animeList: List<AnimeEntity>) {
        replacedList = animeList
    }

    override suspend fun clearAnimeList() {
        storedList = emptyList()
    }

    override suspend fun insertAnimeDetail(animeDetail: AnimeDetailEntity) {
        insertedDetail = animeDetail
    }

    override suspend fun getAnimeDetail(animeId: Int): AnimeDetailEntity? {
        return cachedDetail
    }
}