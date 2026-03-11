package com.devx.data.remote

import com.devx.data.remote.dto.AnimeListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApiService {

    @GET("top/anime")
    suspend fun getAnimeList(@Query("page") page: Int): AnimeListResponse
}
