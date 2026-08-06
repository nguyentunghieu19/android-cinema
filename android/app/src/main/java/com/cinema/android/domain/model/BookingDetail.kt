package com.cinema.android.domain.model

import java.time.LocalDateTime

data class BookingDetail(
    val bookingId: Int,
    val bookingCode: String,
    val movieName: String,
    val cinemaName: String,
    val roomName: String,
    val showtime: LocalDateTime,
    val seats: List<String>,
    val totalAmount: Double,
    val bookingStatus: BookingStatus,
    val paymentStatus: String?,
    val paymentMethod: String?
)