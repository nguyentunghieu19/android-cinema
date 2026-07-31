package com.cinema.android.data.repository

import com.cinema.android.data.remote.api.BookingApi
import com.cinema.android.data.remote.dto.CreateBookingRequestDto
import com.cinema.android.domain.model.BookingResult
import com.cinema.android.domain.repository.BookingRepository
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val bookingApi: BookingApi
) : BookingRepository {

    override suspend fun createBooking(
        showtimeId: Int,
        seatIds: List<Int>,
        paymentMethod: String
    ): Result<BookingResult> {
        return try {
            val response = bookingApi.createBooking(
                CreateBookingRequestDto(
                    showtimeId = showtimeId,
                    seatIds = seatIds,
                    paymentMethod = paymentMethod
                )
            )
            Result.success(
                BookingResult(
                    bookingId = response.bookingId,
                    bookingCode = response.bookingCode,
                    totalAmount = response.totalAmount,
                    paymentUrl = response.paymentUrl
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}