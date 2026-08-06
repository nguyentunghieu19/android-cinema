package com.cinema.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MyBookingResponseDto(
    @SerializedName("bookingId") val bookingId: Int,
    @SerializedName("bookingCode") val bookingCode: String,
    @SerializedName("movieName") val movieName: String,
    @SerializedName("cinemaName") val cinemaName: String,
    @SerializedName("roomName") val roomName: String,
    @SerializedName("showtime") val showtime: String,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("status") val status: String?
)