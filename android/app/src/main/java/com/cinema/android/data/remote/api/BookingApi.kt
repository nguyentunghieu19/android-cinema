package com.cinema.android.data.remote.api

import com.cinema.android.data.remote.dto.BookingDetailResponseDto
import com.cinema.android.data.remote.dto.CreateBookingRequestDto
import com.cinema.android.data.remote.dto.CreateBookingResponseDto
import com.cinema.android.data.remote.dto.MyBookingResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

interface BookingApi {

    @POST("api/bookings")
    suspend fun createBooking(@Body request: CreateBookingRequestDto): CreateBookingResponseDto

    @GET("api/bookings/{id}")
    suspend fun getBookingDetail(@Path("id") id: Int): BookingDetailResponseDto

    @GET("api/bookings/my")
    suspend fun getMyBookings(): List<MyBookingResponseDto>
}