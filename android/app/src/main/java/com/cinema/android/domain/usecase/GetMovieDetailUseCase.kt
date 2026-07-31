package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.Movie
import com.cinema.android.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<Movie> {
        return movieRepository.getMovieById(movieId)
    }
}