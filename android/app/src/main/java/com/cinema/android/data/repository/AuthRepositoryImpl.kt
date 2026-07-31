package com.cinema.android.data.repository

import com.cinema.android.data.datastore.TokenManager
import com.cinema.android.data.remote.api.AuthApi
import com.cinema.android.data.remote.api.UserApi
import com.cinema.android.data.remote.dto.LoginRequestDto
import com.cinema.android.data.remote.dto.UserResponseDto
import com.cinema.android.data.remote.dto.RegisterRequestDto
import com.cinema.android.domain.model.User
import com.cinema.android.domain.model.UserRole
import com.cinema.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val authResponse = authApi.login(
                LoginRequestDto(username = username, password = password)
            )
            tokenManager.saveToken(authResponse.token)

            val userDto = userApi.getCurrentUser()
            Result.success(userDto.toDomain())

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenManager.clearToken()
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val userDto = userApi.getCurrentUser()
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return tokenManager.tokenFlow.map { token -> !token.isNullOrEmpty() }
    }

    private fun UserResponseDto.toDomain(): User {
        return User(
            id = id,
            username = username,
            email = email,
            phone = phone,
            fullname = fullname,
            avatar = avatar,
            isActive = status ?: true,
            role = UserRole.fromString(role)
        )
    }
    override suspend fun register(
        username: String,
        password: String,
        email: String?,
        phone: String?,
        fullname: String?
    ): Result<User> {
        return try {
            val userDto = authApi.register(
                RegisterRequestDto(
                    username = username,
                    password = password,
                    email = email,
                    phone = phone,
                    fullname = fullname
                )
            )
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}