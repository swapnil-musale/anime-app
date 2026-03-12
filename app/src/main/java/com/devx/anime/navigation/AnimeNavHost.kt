package com.devx.anime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devx.anime.feature.animeDetail.AnimeDetailScreen
import com.devx.anime.feature.animeList.AnimeListScreen

@Composable
fun AnimeNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.AnimeList,
    ) {
        composable<AppScreen.AnimeList> {
            AnimeListScreen(
                onAnimeClick = { animeId ->
                    navController.navigate(AppScreen.AnimeDetail(animeId = animeId))
                }
            )
        }

        composable<AppScreen.AnimeDetail> {
            AnimeDetailScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}
