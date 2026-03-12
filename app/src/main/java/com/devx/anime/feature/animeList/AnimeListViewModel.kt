package com.devx.anime.feature.animeList

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devx.domain.core.ConnectivityManager
import com.devx.domain.model.Anime
import com.devx.domain.usecase.FetchAnimeListUseCase
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
    private val fetchAnimeList: FetchAnimeListUseCase,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {

    private data class PaginationState(
        val isLoadingMore: Boolean = false,
        val hasReachedEnd: Boolean = false,
    )

    private var isOffline = false
    private var pagination = PaginationState()

    private val _uiState = MutableStateFlow<AnimeListUiState>(AnimeListUiState.Loading)
    val uiState: StateFlow<AnimeListUiState> = _uiState.asStateFlow()

    init {
        observeConnectivity()
        observeAnimeList()
        loadInitialPage()
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
                    _uiState.value = AnimeListUiState.Success(
                        animeList = list,
                        isOffline = isOffline,
                        isLoadingMore = (_uiState.value as? AnimeListUiState.Success)?.isLoadingMore
                            ?: false,
                    )
                } else {
                    _uiState.value = AnimeListUiState.Empty
                }
            }.launchIn(viewModelScope)
    }

    private fun loadInitialPage() {
        viewModelScope.launch {
            if (_uiState.value !is AnimeListUiState.Success) {
                _uiState.value = AnimeListUiState.Loading
            }

            pagination = PaginationState()
            fetchAnimeList.invoke(isRefresh = true)
                .onSuccess { info ->
                    pagination = PaginationState(hasReachedEnd = !info.hasNextPage)
                }
                .onFailure { error ->
                    if (_uiState.value !is AnimeListUiState.Success) {
                        _uiState.value = AnimeListUiState.Error(
                            message = error.message ?: "Something went wrong",
                            isOffline = isOffline,
                        )
                    }
                }
        }
    }

    fun loadNextPage() {
        val snapshot = pagination
        if (snapshot.isLoadingMore || snapshot.hasReachedEnd) return

        pagination = snapshot.copy(isLoadingMore = true)
        val currentUiState = _uiState.value
        if (currentUiState is AnimeListUiState.Success) {
            _uiState.value = currentUiState.copy(isLoadingMore = true)
        }

        viewModelScope.launch {
            fetchAnimeList.invoke(isRefresh = false)
                .onSuccess { info ->
                    pagination = PaginationState(
                        isLoadingMore = false,
                        hasReachedEnd = !info.hasNextPage,
                    )

                    val currentUiState = _uiState.value
                    if (currentUiState is AnimeListUiState.Success) {
                        _uiState.value = currentUiState.copy(isLoadingMore = false)
                    }
                }
                .onFailure {
                    pagination = snapshot.copy(isLoadingMore = false)
                    val currentUiState = _uiState.value
                    if (currentUiState is AnimeListUiState.Success) {
                        _uiState.value = currentUiState.copy(isLoadingMore = false)
                    }
                }
        }
    }

    fun retry() {
        loadInitialPage()
    }

    fun refresh() {
        val currentUiState = _uiState.value as? AnimeListUiState.Success ?: return
        if (currentUiState.isRefreshing) return

        pagination = PaginationState()
        _uiState.value = currentUiState.copy(isRefreshing = true, isLoadingMore = false)

        viewModelScope.launch {
            fetchAnimeList.invoke(isRefresh = true)
                .onSuccess { info ->
                    pagination = PaginationState(hasReachedEnd = !info.hasNextPage)
                }

            val currentUiState = _uiState.value
            if (currentUiState is AnimeListUiState.Success) {
                _uiState.value = currentUiState.copy(isRefreshing = false)
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
        val isLoadingMore: Boolean = false,
    ) : AnimeListUiState()

    data class Error(
        val message: String,
        val isOffline: Boolean = false,
    ) : AnimeListUiState()
}
