package com.cinema.android.data.repository

import com.cinema.android.data.remote.api.BookingApi
import com.cinema.android.data.remote.dto.BookingDetailResponseDto
import com.cinema.android.data.remote.dto.CreateBookingRequestDto
import com.cinema.android.data.remote.dto.MyBookingResponseDto
import com.cinema.android.domain.model.BookingDetail
import com.cinema.android.domain.model.BookingResult
import com.cinema.android.domain.model.BookingStatus
import com.cinema.android.domain.model.MyBooking
import com.cinema.android.domain.repository.BookingRepository
import java.time.LocalDateTime
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

    override suspend fun getBookingDetail(bookingId: Int): Result<BookingDetail> {
        return try {
            val detail = bookingApi.getBookingDetail(bookingId).toDomain()
            Result.success(detail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyBookings(): Result<List<MyBooking>> {
        return try {
            val bookings = bookingApi.getMyBookings().map { it.toDomain() }
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun BookingDetailResponseDto.toDomain(): BookingDetail {
        return BookingDetail(
            bookingId = bookingId,
            bookingCode = bookingCode,
            movieName = movieName,
            cinemaName = cinemaName,
            roomName = roomName,
            showtime = LocalDateTime.parse(showtime),
            seats = seats,
            totalAmount = totalAmount,
            bookingStatus = BookingStatus.fromString(bookingStatus),
            paymentStatus = paymentStatus,
            paymentMethod = paymentMethod
        )
    }

    private fun MyBookingResponseDto.toDomain(): MyBooking {
        return MyBooking(
            bookingId = bookingId,
            bookingCode = bookingCode,
            movieName = movieName,
            cinemaName = cinemaName,
            roomName = roomName,
            showtime = LocalDateTime.parse(showtime),
            totalAmount = totalAmount,
            status = BookingStatus.fromString(status)
        )
    }
}