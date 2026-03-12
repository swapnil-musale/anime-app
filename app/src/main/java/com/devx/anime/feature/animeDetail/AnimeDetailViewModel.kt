package com.devx.anime.feature.animeDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devx.anime.navigation.AppScreen
import com.devx.domain.model.AnimeDetail
import com.devx.domain.usecase.GetAnimeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAnimeDetail: GetAnimeDetailUseCase,
) : ViewModel() {

    private val animeId: Int = savedStateHandle.toRoute<AppScreen.AnimeDetail>().animeId

    private val _uiState = MutableStateFlow<AnimeDetailUiState>(AnimeDetailUiState.Loading)
    val uiState: StateFlow<AnimeDetailUiState> = _uiState.asStateFlow()

    init {
        loadAnimeDetail()
    }

    private fun loadAnimeDetail() {
        viewModelScope.launch {
            _uiState.value = AnimeDetailUiState.Loading

            getAnimeDetail(animeId = animeId)
                .onSuccess { detail ->
                    _uiState.value = AnimeDetailUiState.Success(animeDetail = detail)
                }
                .onFailure { error ->
                    _uiState.value =
                        AnimeDetailUiState.Error(message = error.message ?: "Something went wrong")
                }
        }
    }

    fun retry() {
        loadAnimeDetail()
    }
}

sealed class AnimeDetailUiState {
    data object Loading : AnimeDetailUiState()
    data class Success(val animeDetail: AnimeDetail) : AnimeDetailUiState()
    data class Error(val message: String) : AnimeDetailUiState()
}
