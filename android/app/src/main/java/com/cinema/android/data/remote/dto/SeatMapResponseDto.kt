// SeatMapResponseDto.kt
package com.cinema.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SeatMapResponseDto(
    @SerializedName("seatId") val seatId: Int,
    @SerializedName("seatRow") val seatRow: String,
    @SerializedName("seatNumber") val seatNumber: Int,
    @SerializedName("status") val status: String?
)