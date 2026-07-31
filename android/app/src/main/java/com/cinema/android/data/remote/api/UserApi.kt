package com.cinema.android.data.remote.api

import com.cinema.android.data.remote.dto.UserResponseDto
import retrofit2.http.GET

interface UserApi {

    @GET("api/users/me")
    suspend fun getCurrentUser(): UserResponseDto
}