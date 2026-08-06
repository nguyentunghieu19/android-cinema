// CreatePaymentRequestDto.kt
package com.cinema.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreatePaymentRequestDto(
    @SerializedName("bookingId") val bookingId: Int
)