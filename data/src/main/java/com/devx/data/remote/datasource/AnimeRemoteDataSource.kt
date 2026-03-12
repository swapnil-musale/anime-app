package com.devx.data.remote.datasource

import com.devx.data.remote.dto.animeDetail.AnimeDetailResponse
import com.devx.data.remote.dto.animeList.AnimeListResponse
import com.devx.data.remote.util.NetworkResult

interface AnimeRemoteDataSource {
    suspend fun fetchTopAnime(page: Int): NetworkResult<AnimeListResponse>
    suspend fun fetchAnimeDetail(animeId: Int): NetworkResult<AnimeDetailResponse>
}
