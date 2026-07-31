package com.cinema.android.ui.viewmodel

import com.cinema.android.domain.model.Movie

sealed interface MovieListUiState {
    data object Loading : MovieListUiState
    data class Success(val movies: List<Movie>) : MovieListUiState
    data class Error(val message: String) : MovieListUiState
}

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(val movie: Movie) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}