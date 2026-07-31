package com.cinema.android.domain.repository

import com.cinema.android.domain.model.User

interface AuthRepository {

    suspend fun login(username: String, password: String): Result<User>
    suspend fun register(
        username: String,
        password: String,
        email: String?,
        phone: String?,
        fullname: String?
    ): Result<User>
    suspend fun logout()

    suspend fun getCurrentUser(): Result<User>

    fun isLoggedIn(): kotlinx.coroutines.flow.Flow<Boolean>
}