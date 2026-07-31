package com.cinema.android.domain.repository

import com.cinema.android.domain.model.Seat
import com.cinema.android.domain.model.Showtime

interface ShowtimeRepository {

    suspend fun getShowtimesByMovie(movieId: Int): Result<List<Showtime>>

    suspend fun getShowtimeById(showtimeId: Int): Result<Showtime>
    suspend fun getSeatMap(showtimeId: Int): Result<List<Seat>>
}