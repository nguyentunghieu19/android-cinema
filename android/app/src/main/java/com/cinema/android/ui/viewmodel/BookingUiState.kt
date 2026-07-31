package com.cinema.android.ui.viewmodel

import com.cinema.android.domain.model.Seat
import com.cinema.android.domain.model.Showtime

sealed interface ShowtimeListUiState {
    data object Loading : ShowtimeListUiState
    data class Success(val showtimesByCinema: Map<String, List<Showtime>>) : ShowtimeListUiState
    data class Error(val message: String) : ShowtimeListUiState
}

sealed interface SeatSelectionUiState {
    data object Loading : SeatSelectionUiState
    data class Success(val showtime: Showtime, val seats: List<Seat>) : SeatSelectionUiState
    data class Error(val message: String) : SeatSelectionUiState

}
sealed interface CreateBookingUiState {
    data object Idle : CreateBookingUiState
    data object Loading : CreateBookingUiState
    data class Success(val result: com.cinema.android.domain.model.BookingResult) : CreateBookingUiState
    data class Error(val message: String) : CreateBookingUiState
}