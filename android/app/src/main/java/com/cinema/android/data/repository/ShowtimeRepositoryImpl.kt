package com.cinema.android.data.repository

import com.cinema.android.data.remote.api.ShowtimeApi
import com.cinema.android.data.remote.dto.SeatMapResponseDto
import com.cinema.android.data.remote.dto.ShowtimeResponseDto
import com.cinema.android.domain.model.Seat
import com.cinema.android.domain.model.SeatStatus
import com.cinema.android.domain.model.Showtime
import com.cinema.android.domain.repository.ShowtimeRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ShowtimeRepositoryImpl @Inject constructor(
    private val showtimeApi: ShowtimeApi
) : ShowtimeRepository {

    override suspend fun getShowtimesByMovie(movieId: Int): Result<List<Showtime>> {
        return try {
            val showtimes = showtimeApi.getShowtimesByMovie(movieId).map { it.toDomain() }
            Result.success(showtimes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getShowtimeById(showtimeId: Int): Result<Showtime> {
        return try {
            val showtime = showtimeApi.getShowtimeById(showtimeId).toDomain()
            Result.success(showtime)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSeatMap(showtimeId: Int): Result<List<Seat>> {
        return try {
            val seats = showtimeApi.getSeatMap(showtimeId).map { it.toDomain() }
            Result.success(seats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun ShowtimeResponseDto.toDomain(): Showtime {
        return Showtime(
            id = id,
            movieId = movieId,
            movieTitle = movieTitle,
            roomId = roomId,
            roomName = roomName,
            cinemaId = cinemaId,
            cinemaName = cinemaName,
            startTime = LocalDateTime.parse(startTime),
            endTime = LocalDateTime.parse(endTime),
            price = price
        )
    }

    private fun SeatMapResponseDto.toDomain(): Seat {
        return Seat(
            id = seatId,
            row = seatRow,
            number = seatNumber,
            status = SeatStatus.fromString(status)
        )
    }
}