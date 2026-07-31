package com.cinema.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinema.android.domain.usecase.GetMovieDetailUseCase
import com.cinema.android.domain.usecase.GetMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val _movieListState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val movieListState: StateFlow<MovieListUiState> = _movieListState.asStateFlow()

    private val _movieDetailState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val movieDetailState: StateFlow<MovieDetailUiState> = _movieDetailState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _movieListState.value = MovieListUiState.Loading
            val result = getMovieListUseCase()
            _movieListState.value = result.fold(
                onSuccess = { movies -> MovieListUiState.Success(movies) },
                onFailure = { error -> MovieListUiState.Error(error.message ?: "Không tải được danh sách phim") }
            )
        }
    }

    fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _movieDetailState.value = MovieDetailUiState.Loading
            val result = getMovieDetailUseCase(movieId)
            _movieDetailState.value = result.fold(
                onSuccess = { movie -> MovieDetailUiState.Success(movie) },
                onFailure = { error -> MovieDetailUiState.Error(error.message ?: "Không tải được chi tiết phim") }
            )
        }
    }
}