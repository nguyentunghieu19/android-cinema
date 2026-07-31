// CreateBookingRequestDto.kt
package com.cinema.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateBookingRequestDto(
    @SerializedName("showtimeId") val showtimeId: Int,
    @SerializedName("seatIds") val seatIds: List<Int>,
    @SerializedName("paymentMethod") val paymentMethod: String
)