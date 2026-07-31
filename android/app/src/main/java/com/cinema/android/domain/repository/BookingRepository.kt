package com.cinema.android.domain.repository

import com.cinema.android.domain.model.BookingResult

interface BookingRepository {

    suspend fun createBooking(
        showtimeId: Int,
        seatIds: List<Int>,
        paymentMethod: String
    ): Result<BookingResult>
}