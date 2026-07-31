package com.cinema.android.data.remote.api

import com.cinema.android.data.remote.dto.LoginRequestDto
import com.cinema.android.data.remote.dto.LoginResponseDto
import com.cinema.android.data.remote.dto.RegisterRequestDto
import com.cinema.android.data.remote.dto.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): UserResponseDto
}