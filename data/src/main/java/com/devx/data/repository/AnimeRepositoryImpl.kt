package com.devx.data.repository

import com.devx.data.core.ConnectivityManagerImpl
import com.devx.data.local.datasource.AnimeLocalDataSource
import com.devx.data.local.mapper.mapToDomain
import com.devx.data.local.mapper.toEntity
import com.devx.data.remote.datasource.AnimeRemoteDataSource
import com.devx.data.remote.util.NetworkResult
import com.devx.domain.core.ConnectivityManager
import com.devx.domain.model.Anime
import com.devx.domain.model.AnimeDetail
import com.devx.domain.model.PaginationInfo
import com.devx.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val remoteDataSource: AnimeRemoteDataSource,
    private val localDataSource: AnimeLocalDataSource,
    private val connectivityManagerImpl: ConnectivityManagerImpl,
) : AnimeRepository {

    private var currentPage = 0

    override suspend fun fetchAnimePage(isRefresh: Boolean): Result<PaginationInfo> {
        if (connectivityManagerImpl.status.value != ConnectivityManager.Status.AVAILABLE) {
            val hasCachedData = localDataSource.getAnimeList().isNotEmpty()
            return if (hasCachedData) {
                Result.success(value = PaginationInfo(hasNextPage = false))
            } else {
                Result.failure(exception = IllegalStateException("No internet connection"))
            }
        }

        if (isRefresh) {
            currentPage = 0
        }

        val pageToFetch = currentPage + 1
        return when (val result = remoteDataSource.fetchTopAnime(page = pageToFetch)) {
            is NetworkResult.Success -> {
                currentPage = pageToFetch
                val entities = result.data.animeList.map { it.toEntity() }
                if (isRefresh) {
                    localDataSource.replaceAnimeList(entities)
                } else {
                    localDataSource.insertAnimeList(entities)
                }
                Result.success(value = PaginationInfo(hasNextPage = result.data.pagination.hasNextPage))
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

        if (connectivityManagerImpl.status.value != ConnectivityManager.Status.AVAILABLE) {
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
