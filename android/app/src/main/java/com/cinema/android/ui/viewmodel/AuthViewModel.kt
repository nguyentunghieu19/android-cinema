package com.cinema.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinema.android.domain.usecase.LoginUseCase
import com.cinema.android.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            val result = loginUseCase(username, password)
            _loginState.value = result.fold(
                onSuccess = { user -> LoginUiState.Success(user) },
                onFailure = { error -> LoginUiState.Error(error.message ?: "Đã có lỗi xảy ra") }
            )
        }
    }

    fun register(
        username: String,
        password: String,
        email: String?,
        phone: String?,
        fullname: String?
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterUiState.Loading
            val result = registerUseCase(username, password, email, phone, fullname)
            _registerState.value = result.fold(
                onSuccess = { user -> RegisterUiState.Success(user) },
                onFailure = { error -> RegisterUiState.Error(error.message ?: "Đã có lỗi xảy ra") }
            )
        }
    }
}