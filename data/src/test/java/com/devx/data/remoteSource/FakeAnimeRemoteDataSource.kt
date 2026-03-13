package com.devx.data.remoteSource

import com.devx.data.remote.datasource.AnimeRemoteDataSource
import com.devx.data.remote.dto.animeDetail.AnimeDetailResponse
import com.devx.data.remote.dto.animeList.AnimeListResponse
import com.devx.data.remote.util.NetworkResult

class FakeAnimeRemoteDataSource : AnimeRemoteDataSource {
    var topAnimeResult: NetworkResult<AnimeListResponse> =
        NetworkResult.Exception(IllegalStateException("topAnimeResult not configured"))
    var animeDetailResult: NetworkResult<AnimeDetailResponse> =
        NetworkResult.Exception(IllegalStateException("animeDetailResult not configured"))

    var fetchTopAnimeCalled = false
    var fetchAnimeDetailCalled = false

    override suspend fun fetchTopAnime(page: Int): NetworkResult<AnimeListResponse> {
        fetchTopAnimeCalled = true
        return topAnimeResult
    }

    override suspend fun fetchAnimeDetail(animeId: Int): NetworkResult<AnimeDetailResponse> {
        fetchAnimeDetailCalled = true
        return animeDetailResult
    }
}