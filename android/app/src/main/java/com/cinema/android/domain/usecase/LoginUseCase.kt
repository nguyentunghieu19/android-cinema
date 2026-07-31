package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.User
import com.cinema.android.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(username: String, password: String): Result<User> {

        if (username.isBlank()) {
            return Result.failure(IllegalArgumentException("Tên đăng nhập không được để trống"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Mật khẩu không được để trống"))
        }

        return authRepository.login(username.trim(), password)
    }
}