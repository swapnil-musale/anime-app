package com.devx.data.local.datasource

import com.devx.data.local.dao.AnimeDao
import com.devx.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeLocalDataSourceImpl @Inject constructor(
    private val animeDao: AnimeDao,
) : AnimeLocalDataSource {

    override fun observeAnimeList(): Flow<List<AnimeEntity>> {
        return animeDao.observeAll()
    }

    override suspend fun insertAnimeList(animeList: List<AnimeEntity>) {
        animeDao.insertAll(animeList = animeList)
    }

    override suspend fun clearAnimeList() {
        animeDao.clearAll()
    }
}
