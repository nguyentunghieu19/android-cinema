package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.BookingResult
import com.cinema.android.domain.repository.BookingRepository
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(
        showtimeId: Int,
        seatIds: List<Int>,
        paymentMethod: String
    ): Result<BookingResult> {

        if (seatIds.isEmpty()) {
            return Result.failure(IllegalArgumentException("Vui long chon it nhat 1 ghe"))
        }

        return bookingRepository.createBooking(showtimeId, seatIds, paymentMethod)
    }
}