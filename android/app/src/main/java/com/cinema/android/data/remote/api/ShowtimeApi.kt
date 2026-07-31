package com.cinema.android.data.remote.api

import com.cinema.android.data.remote.dto.SeatMapResponseDto
import com.cinema.android.data.remote.dto.ShowtimeResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ShowtimeApi {

    @GET("api/movies/{movieId}/showtimes")
    suspend fun getShowtimesByMovie(@Path("movieId") movieId: Int): List<ShowtimeResponseDto>
    @GET("api/showtimes/{id}")
    suspend fun getShowtimeById(@Path("id") id: Int): ShowtimeResponseDto

    @GET("api/showtimes/{id}/seats")
    suspend fun getSeatMap(@Path("id") showtimeId: Int): List<SeatMapResponseDto>
}