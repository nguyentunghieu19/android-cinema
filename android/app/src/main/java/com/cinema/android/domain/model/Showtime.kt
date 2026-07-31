package com.cinema.android.domain.model

import java.time.LocalDateTime

data class Showtime(
    val id: Int,
    val movieId: Int,
    val movieTitle: String,
    val roomId: Int,
    val roomName: String,
    val cinemaId: Int,
    val cinemaName: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val price: Double
)