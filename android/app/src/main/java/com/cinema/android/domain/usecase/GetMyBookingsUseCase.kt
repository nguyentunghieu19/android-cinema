package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.MyBooking
import com.cinema.android.domain.repository.BookingRepository
import javax.inject.Inject

class GetMyBookingsUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(): Result<List<MyBooking>> {
        return bookingRepository.getMyBookings()
    }
}