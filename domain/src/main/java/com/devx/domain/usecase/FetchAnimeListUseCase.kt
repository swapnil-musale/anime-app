package com.devx.domain.usecase

import com.devx.domain.model.PaginationInfo
import com.devx.domain.repository.AnimeRepository
import javax.inject.Inject

class FetchAnimeListUseCase @Inject constructor(
    private val animeRepository: AnimeRepository,
) {
    suspend operator fun invoke(isRefresh: Boolean = false): Result<PaginationInfo> =
        animeRepository.fetchAnimePage(isRefresh = isRefresh)
}
