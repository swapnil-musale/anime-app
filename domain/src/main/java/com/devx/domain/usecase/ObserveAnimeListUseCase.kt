package com.devx.domain.usecase

import com.devx.domain.model.Anime
import com.devx.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAnimeListUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    operator fun invoke(): Flow<List<Anime>> {
        return animeRepository.observeAnimeList()
    }
}
