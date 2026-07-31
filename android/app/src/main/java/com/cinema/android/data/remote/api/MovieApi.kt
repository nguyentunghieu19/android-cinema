package com.cinema.android.data.remote.api

import com.cinema.android.data.remote.dto.MovieResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {

    @GET("api/movies")
    suspend fun getAllMovies(): List<MovieResponseDto>

    @GET("api/movies/{id}")
    suspend fun getMovieById(@Path("id") id: Int): MovieResponseDto
}