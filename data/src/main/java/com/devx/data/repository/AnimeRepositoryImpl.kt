package com.devx.data.repository

import com.devx.data.local.datasource.AnimeLocalDataSource
import com.devx.data.local.mapper.mapToDomain
import com.devx.data.local.mapper.toEntity
import com.devx.data.remote.datasource.AnimeRemoteDataSource
import com.devx.data.remote.util.ConnectivityManager
import com.devx.data.remote.util.NetworkResult
import com.devx.domain.model.Anime
import com.devx.domain.model.AnimeDetail
import com.devx.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val remoteDataSource: AnimeRemoteDataSource,
    private val localDataSource: AnimeLocalDataSource,
    private val connectivityManager: ConnectivityManager,
) : AnimeRepository {

    override suspend fun fetchTopAnime(page: Int): Result<Unit> {
        if (connectivityManager.status.value != ConnectivityManager.Status.AVAILABLE) {
            return Result.failure(exception = IllegalStateException("No internet connection"))
        }

        return when (val result = remoteDataSource.fetchTopAnime(page = page)) {
            is NetworkResult.Success -> {
                if (page == 1) {
                    localDataSource.clearAnimeList()
                }
                localDataSource.insertAnimeList(result.data.animeList.map { it.toEntity() })
                Result.success(value = Unit)
            }
            is NetworkResult.Error -> Result.failure(exception = IllegalStateException(result.message))
            is NetworkResult.Exception -> Result.failure(exception = result.throwable)
        }
    }

    override fun observeAnimeList(): Flow<List<Anime>> {
        return localDataSource.observeAnimeList().map { entities ->
            entities.map { it.mapToDomain() }
        }
    }

    override suspend fun fetchAnimeDetail(animeId: Int): Result<AnimeDetail> {
        val cached = localDataSource.getAnimeDetail(animeId = animeId)

        if (connectivityManager.status.value != ConnectivityManager.Status.AVAILABLE) {
            return if (cached != null) {
                Result.success(value = cached.mapToDomain())
            } else {
                Result.failure(exception = IllegalStateException("No internet connection and no cached data"))
            }
        }

        return when (val result = remoteDataSource.fetchAnimeDetail(animeId = animeId)) {
            is NetworkResult.Success -> {
                val entity = result.data.animeDetail.toEntity()
                localDataSource.insertAnimeDetail(animeDetail = entity)
                Result.success(value = entity.mapToDomain())
            }
            is NetworkResult.Error -> {
                if (cached != null) Result.success(value = cached.mapToDomain())
                else Result.failure(exception = IllegalStateException(result.message))
            }
            is NetworkResult.Exception -> {
                if (cached != null) Result.success(value = cached.mapToDomain())
                else Result.failure(exception = result.throwable)
            }
        }
    }
}
