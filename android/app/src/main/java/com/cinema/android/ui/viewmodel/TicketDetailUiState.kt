package com.cinema.android.ui.viewmodel

import com.cinema.android.domain.model.BookingDetail

sealed interface TicketDetailUiState {
    data object Loading : TicketDetailUiState
    data class Success(val booking: BookingDetail) : TicketDetailUiState
    data class Error(val message: String) : TicketDetailUiState
}