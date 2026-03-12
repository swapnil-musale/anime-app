package com.devx.anime.feature.animeList

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devx.domain.core.ConnectivityManager
import com.devx.domain.model.Anime
import com.devx.domain.usecase.FetchTopAnimeUseCase
import com.devx.domain.usecase.ObserveAnimeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val observeAnimeList: ObserveAnimeListUseCase,
    private val fetchTopAnime: FetchTopAnimeUseCase,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {

    private var isOffline = false

    private val _uiState = MutableStateFlow<AnimeListUiState>(AnimeListUiState.Loading)
    val uiState: StateFlow<AnimeListUiState> = _uiState.asStateFlow()

    init {
        observeConnectivity()
        observeAnimeList()
        fetchAnimeList()
    }

    private fun observeConnectivity() {
        connectivityManager.status
            .onEach { status ->
                isOffline = status != ConnectivityManager.Status.AVAILABLE

                val currentUiState = _uiState.value
                if (currentUiState is AnimeListUiState.Success) {
                    _uiState.value = currentUiState.copy(isOffline = isOffline)
                } else if (currentUiState is AnimeListUiState.Error) {
                    _uiState.value = currentUiState.copy(isOffline = isOffline)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeAnimeList() {
        observeAnimeList.invoke()
            .onEach { list ->
                if (list.isNotEmpty()) {
                    _uiState.value =
                        AnimeListUiState.Success(animeList = list, isOffline = isOffline)
                } else {
                    _uiState.value = AnimeListUiState.Empty
                }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchAnimeList() {
        viewModelScope.launch {
            if (_uiState.value !is AnimeListUiState.Success) {
                _uiState.value = AnimeListUiState.Loading
            }

            fetchTopAnime.invoke(page = 1).onFailure { error ->
                if (_uiState.value !is AnimeListUiState.Success) {
                    _uiState.value = AnimeListUiState.Error(
                        message = error.message ?: "Something went wrong",
                        isOffline = isOffline,
                    )
                }
            }
        }
    }

    fun retry() {
        fetchAnimeList()
    }

    fun refresh() {
        val current = _uiState.value as? AnimeListUiState.Success ?: return
        if (current.isRefreshing) return
        _uiState.value = current.copy(isRefreshing = true)
        viewModelScope.launch {
            fetchTopAnime.invoke(page = 1)
            val latest = _uiState.value
            if (latest is AnimeListUiState.Success) {
                _uiState.value = latest.copy(isRefreshing = false)
            }
        }
    }
}

sealed class AnimeListUiState {
    data object Loading : AnimeListUiState()
    data object Empty : AnimeListUiState()

    @Immutable
    data class Success(
        val animeList: List<Anime>,
        val isOffline: Boolean = false,
        val isRefreshing: Boolean = false,
    ) : AnimeListUiState()

    data class Error(
        val message: String,
        val isOffline: Boolean = false,
    ) : AnimeListUiState()
}
