package com.cinema.android.ui.viewmodel

import com.cinema.android.domain.model.MyBooking
import com.cinema.android.domain.model.User

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val user: User) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

sealed interface BookingHistoryUiState {
    data object Loading : BookingHistoryUiState
    data class Success(val bookings: List<MyBooking>) : BookingHistoryUiState
    data class Error(val message: String) : BookingHistoryUiState
}

