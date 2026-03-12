package com.devx.data.local.datasource

import com.devx.data.local.dao.AnimeDao
import com.devx.data.local.dao.AnimeDetailDao
import com.devx.data.local.entity.AnimeDetailEntity
import com.devx.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeLocalDataSourceImpl @Inject constructor(
    private val animeDao: AnimeDao,
    private val animeDetailDao: AnimeDetailDao,
) : AnimeLocalDataSource {

    override fun observeAnimeList(): Flow<List<AnimeEntity>> {
        return animeDao.observeAll()
    }

    override suspend fun getAnimeList(): List<AnimeEntity> {
        return animeDao.getAll()
    }

    override suspend fun insertAnimeList(animeList: List<AnimeEntity>) {
        animeDao.insertAll(animeList = animeList)
    }

    override suspend fun clearAnimeList() {
        animeDao.clearAll()
    }

    override suspend fun insertAnimeDetail(animeDetail: AnimeDetailEntity) {
        animeDetailDao.insert(animeDetail = animeDetail)
    }

    override suspend fun getAnimeDetail(animeId: Int): AnimeDetailEntity? {
        return animeDetailDao.getById(animeId = animeId)
    }
}
