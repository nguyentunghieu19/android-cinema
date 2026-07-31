package com.cinema.android.data.remote.api

import com.cinema.android.data.remote.dto.CreateBookingRequestDto
import com.cinema.android.data.remote.dto.CreateBookingResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface BookingApi {

    @POST("api/bookings")
    suspend fun createBooking(@Body request: CreateBookingRequestDto): CreateBookingResponseDto
}