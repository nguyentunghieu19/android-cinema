package com.cinema.android.domain.model

import java.time.LocalDateTime

data class MyBooking(
    val bookingId: Int,
    val bookingCode: String,
    val movieName: String,
    val cinemaName: String,
    val roomName: String,
    val showtime: LocalDateTime,
    val totalAmount: Double,
    val status: BookingStatus
)