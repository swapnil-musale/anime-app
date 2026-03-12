package com.devx.data.remote.datasource

import com.devx.data.remote.JikanApiService
import com.devx.data.remote.dto.animeDetail.AnimeDetailResponse
import com.devx.data.remote.dto.animeList.AnimeListResponse
import com.devx.data.remote.util.NetworkResult
import com.devx.data.remote.util.safeApiCall
import javax.inject.Inject

class AnimeRemoteDataSourceImpl @Inject constructor(
    private val apiService: JikanApiService,
) : AnimeRemoteDataSource {

    override suspend fun fetchTopAnime(page: Int): NetworkResult<AnimeListResponse> {
        return safeApiCall { apiService.getAnimeList(page = page) }
    }

    override suspend fun fetchAnimeDetail(animeId: Int): NetworkResult<AnimeDetailResponse> {
        return safeApiCall { apiService.getAnimeDetail(animeId = animeId) }
    }
}
