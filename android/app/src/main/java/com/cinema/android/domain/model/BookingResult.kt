package com.cinema.android.domain.model

data class BookingResult(
    val bookingId: Int,
    val bookingCode: String,
    val totalAmount: Double,
    val paymentUrl: String?
)