package com.cinema.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinema.android.domain.usecase.GetMyBookingsUseCase
import com.cinema.android.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: com.cinema.android.domain.usecase.GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getMyBookingsUseCase: GetMyBookingsUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()

    private val _historyState = MutableStateFlow<BookingHistoryUiState>(BookingHistoryUiState.Loading)
    val historyState: StateFlow<BookingHistoryUiState> = _historyState.asStateFlow()

    private val _loggedOut = MutableSharedFlow<Unit>()
    val loggedOut: SharedFlow<Unit> = _loggedOut

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileUiState.Loading
            val result = getCurrentUserUseCase()
            _profileState.value = result.fold(
                onSuccess = { user -> ProfileUiState.Success(user) },
                onFailure = { e -> ProfileUiState.Error(e.message ?: "Khong tai duoc thong tin") }
            )
        }
    }

    fun loadBookingHistory() {
        viewModelScope.launch {
            _historyState.value = BookingHistoryUiState.Loading
            val result = getMyBookingsUseCase()
            _historyState.value = result.fold(
                onSuccess = { list -> BookingHistoryUiState.Success(list.sortedByDescending { it.showtime }) },
                onFailure = { e -> BookingHistoryUiState.Error(e.message ?: "Khong tai duoc lich su") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _loggedOut.emit(Unit)
        }
    }
}