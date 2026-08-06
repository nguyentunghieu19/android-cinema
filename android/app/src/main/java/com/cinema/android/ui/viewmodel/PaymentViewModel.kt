package com.cinema.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinema.android.domain.model.BookingStatus
import com.cinema.android.domain.usecase.CreateVNPayPaymentUseCase
import com.cinema.android.domain.usecase.GetBookingDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val createVNPayPaymentUseCase: CreateVNPayPaymentUseCase,
    private val getBookingDetailUseCase: GetBookingDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PaymentUiState>(PaymentUiState.CreatingPayment)
    val state: StateFlow<PaymentUiState> = _state.asStateFlow()

    fun createPayment(bookingId: Int) {
        viewModelScope.launch {
            _state.value = PaymentUiState.CreatingPayment
            val result = createVNPayPaymentUseCase(bookingId)
            _state.value = result.fold(
                onSuccess = { url -> PaymentUiState.ReadyToOpenBrowser(url) },
                onFailure = { e -> PaymentUiState.Error(e.message ?: "Khong tao duoc thanh toan") }
            )
        }
    }

    fun onBrowserOpened() {
        _state.value = PaymentUiState.WaitingForUser
    }

    fun checkBookingStatus(bookingId: Int) {
        viewModelScope.launch {
            _state.value = PaymentUiState.CheckingStatus
            val result = getBookingDetailUseCase(bookingId)
            _state.value = result.fold(
                onSuccess = { booking ->
                    when (booking.bookingStatus) {
                        BookingStatus.PAID -> PaymentUiState.Success(booking)
                        BookingStatus.CANCELLED -> PaymentUiState.Failed(
                            "Thanh toan khong thanh cong hoac don hang da het han"
                        )
                        else -> PaymentUiState.Pending(booking)
                    }
                },
                onFailure = { e ->
                    PaymentUiState.Error(e.message ?: "Khong kiem tra duoc trang thai")
                }
            )
        }
    }
}