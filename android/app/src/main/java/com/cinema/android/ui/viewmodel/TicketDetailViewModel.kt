package com.cinema.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinema.android.domain.usecase.GetBookingDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    private val getBookingDetailUseCase: GetBookingDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<TicketDetailUiState>(TicketDetailUiState.Loading)
    val state: StateFlow<TicketDetailUiState> = _state.asStateFlow()

    fun loadDetail(bookingId: Int) {
        viewModelScope.launch {
            _state.value = TicketDetailUiState.Loading
            val result = getBookingDetailUseCase(bookingId)
            _state.value = result.fold(
                onSuccess = { booking -> TicketDetailUiState.Success(booking) },
                onFailure = { e -> TicketDetailUiState.Error(e.message ?: "Khong tai duoc chi tiet ve") }
            )
        }
    }
}