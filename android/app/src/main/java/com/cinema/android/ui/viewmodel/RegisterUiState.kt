package com.cinema.android.ui.viewmodel

import com.cinema.android.domain.model.User

sealed interface RegisterUiState {
    data object Idle : RegisterUiState
    data object Loading : RegisterUiState
    data class Success(val user: User) : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}