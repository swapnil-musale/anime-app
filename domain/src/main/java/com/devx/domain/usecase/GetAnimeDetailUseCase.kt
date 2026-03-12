package com.devx.domain.usecase

import com.devx.domain.model.AnimeDetail
import com.devx.domain.repository.AnimeRepository
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(
    private val animeRepository: AnimeRepository,
) {
    suspend operator fun invoke(animeId: Int): Result<AnimeDetail> {
        return animeRepository.fetchAnimeDetail(animeId = animeId)
    }
}
