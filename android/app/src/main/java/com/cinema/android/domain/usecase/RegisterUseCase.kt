package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.User
import com.cinema.android.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        email: String?,
        phone: String?,
        fullname: String?
    ): Result<User> {

        if (username.isBlank()) {
            return Result.failure(IllegalArgumentException("Tên đăng nhập không được để trống"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự"))
        }

        return authRepository.register(
            username = username.trim(),
            password = password,
            email = email?.trim()?.ifBlank { null },
            phone = phone?.trim()?.ifBlank { null },
            fullname = fullname?.trim()?.ifBlank { null }
        )
    }
}