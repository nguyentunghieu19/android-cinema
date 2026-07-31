// CreateBookingResponseDto.kt
package com.cinema.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateBookingResponseDto(
    @SerializedName("bookingId") val bookingId: Int,
    @SerializedName("bookingCode") val bookingCode: String,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("paymentUrl") val paymentUrl: String?
)