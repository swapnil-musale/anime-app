package com.devx.anime.navigation

import kotlinx.serialization.Serializable

sealed class AppScreen {

    @Serializable
    data object AnimeList : AppScreen()

    @Serializable
    data class AnimeDetail(val animeId: Int) : AppScreen()
}
