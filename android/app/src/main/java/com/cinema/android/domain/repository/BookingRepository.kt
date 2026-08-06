package com.cinema.android.domain.repository

import com.cinema.android.domain.model.BookingResult
import com.cinema.android.domain.model.BookingDetail
import com.cinema.android.domain.model.MyBooking
interface BookingRepository {

    suspend fun createBooking(
        showtimeId: Int,
        seatIds: List<Int>,
        paymentMethod: String
    ): Result<BookingResult>
    suspend fun getBookingDetail(bookingId: Int): Result<BookingDetail>

    suspend fun getMyBookings(): Result<List<MyBooking>>
}