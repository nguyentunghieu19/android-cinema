package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.BookingDetail
import com.cinema.android.domain.repository.BookingRepository
import javax.inject.Inject

class GetBookingDetailUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(bookingId: Int): Result<BookingDetail> {
        return bookingRepository.getBookingDetail(bookingId)
    }
}