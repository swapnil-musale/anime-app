package com.devx.anime.feature.animeList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devx.anime.feature.animeList.components.AnimeListItem
import com.devx.anime.ui.theme.AnimeAppTheme
import com.devx.domain.model.Anime

@Composable
fun AnimeListScreen(
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    animeListViewModel: AnimeListViewModel = hiltViewModel(),
) {
    val uiState by animeListViewModel.uiState.collectAsStateWithLifecycle()
    val onRetry = remember(animeListViewModel) {
        { animeListViewModel.retry() }
    }
    val onRefresh = remember(animeListViewModel) {
        { animeListViewModel.refresh() }
    }

    AnimeListContent(
        uiState = uiState,
        onAnimeClick = onAnimeClick,
        onRetry = onRetry,
        onRefresh = onRefresh,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeListContent(
    uiState: AnimeListUiState,
    onAnimeClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Top Anime",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                )

                val isOffline = when (uiState) {
                    is AnimeListUiState.Success -> uiState.isOffline
                    is AnimeListUiState.Error -> uiState.isOffline
                    else -> false
                }

                if (isOffline) {
                    OfflineBanner()
                }
            }
        }
    ) { innerPadding ->
        when (uiState) {
            is AnimeListUiState.Loading -> LoadingContent(
                modifier = Modifier.padding(innerPadding)
            )

            is AnimeListUiState.Success -> PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.padding(innerPadding),
            ) {
                AnimeList(
                    animeList = uiState.animeList,
                    onAnimeClick = onAnimeClick,
                )
            }

            is AnimeListUiState.Error -> ErrorContent(
                message = uiState.message,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding),
            )

            is AnimeListUiState.Empty -> EmptyContent(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun AnimeList(
    animeList: List<Anime>,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = animeList,
            key = { anime -> anime.malId },
            contentType = { "anime_item" },
        ) { anime ->
            AnimeListItem(
                anime = anime,
                onAnimeClick = onAnimeClick,
            )
        }
    }
}

@Composable
private fun OfflineBanner(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer,
    ) {
        Text(
            text = "You're offline — showing cached data",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "No anime found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Check your connection and try again",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimeListLoadingPreview() {
    AnimeAppTheme {
        AnimeListContent(
            uiState = AnimeListUiState.Loading,
            onAnimeClick = {},
            onRetry = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimeListSuccessPreview() {
    AnimeAppTheme {
        AnimeListContent(
            uiState = AnimeListUiState.Success(
                isOffline = false,
                animeList = listOf(
                    Anime(
                        malId = 1,
                        title = "Sousou no Frieren",
                        titleEnglish = "Frieren: Beyond Journey's End",
                        episodes = 28,
                        score = 9.28,
                        imageUrl = "",
                        synopsis = "An elven mage reflects on life after defeating the Demon King."
                    ),
                    Anime(
                        malId = 2,
                        title = "Fullmetal Alchemist: Brotherhood",
                        titleEnglish = "Fullmetal Alchemist: Brotherhood",
                        episodes = 64,
                        score = 9.11,
                        imageUrl = "",
                        synopsis = "Two brothers search for the Philosopher's Stone."
                    ),
                ),
            ),
            onAnimeClick = {},
            onRetry = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimeListOfflinePreview() {
    AnimeAppTheme {
        AnimeListContent(
            uiState = AnimeListUiState.Success(
                isOffline = true,
                animeList = listOf(
                    Anime(
                        malId = 1,
                        title = "Sousou no Frieren",
                        titleEnglish = "Frieren: Beyond Journey's End",
                        episodes = 28,
                        score = 9.28,
                        imageUrl = "",
                        synopsis = null
                    )
                ),
            ),
            onAnimeClick = {},
            onRetry = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimeListErrorPreview() {
    AnimeAppTheme {
        AnimeListContent(
            uiState = AnimeListUiState.Error(message = "No internet connection", isOffline = true),
            onAnimeClick = {},
            onRetry = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimeListEmptyPreview() {
    AnimeAppTheme {
        AnimeListContent(
            uiState = AnimeListUiState.Empty,
            onAnimeClick = {},
            onRetry = {},
            onRefresh = {},
        )
    }
}
