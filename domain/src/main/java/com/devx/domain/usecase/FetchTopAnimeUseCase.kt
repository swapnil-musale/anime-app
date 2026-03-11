package com.devx.domain.usecase

import com.devx.domain.repository.AnimeRepository
import javax.inject.Inject

class FetchTopAnimeUseCase @Inject constructor(
    private val animeRepository: AnimeRepository,
) {
    suspend operator fun invoke(page: Int): Result<Unit> {
        return animeRepository.fetchTopAnime(page = page)
    }
}
