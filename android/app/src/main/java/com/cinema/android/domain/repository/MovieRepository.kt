package com.cinema.android.domain.repository

import com.cinema.android.domain.model.Movie

interface MovieRepository {

    suspend fun getAllMovies(): Result<List<Movie>>

    suspend fun getMovieById(id: Int): Result<Movie>
}