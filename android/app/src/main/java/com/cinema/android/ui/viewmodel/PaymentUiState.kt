package com.cinema.android.ui.viewmodel

import com.cinema.android.domain.model.BookingDetail

sealed interface PaymentUiState {
    data object CreatingPayment : PaymentUiState
    data class ReadyToOpenBrowser(val paymentUrl: String) : PaymentUiState
    data object WaitingForUser : PaymentUiState
    data object CheckingStatus : PaymentUiState
    data class Success(val booking: BookingDetail) : PaymentUiState
    data class Pending(val booking: BookingDetail) : PaymentUiState
    data class Failed(val message: String) : PaymentUiState
    data class Error(val message: String) : PaymentUiState
}