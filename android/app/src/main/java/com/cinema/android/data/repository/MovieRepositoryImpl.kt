package com.cinema.android.data.repository

import com.cinema.android.data.remote.api.MovieApi
import com.cinema.android.data.remote.dto.MovieResponseDto
import com.cinema.android.domain.model.Movie
import com.cinema.android.domain.repository.MovieRepository
import java.time.LocalDate
import javax.inject.Inject

private const val IMAGE_BASE_URL = "http://localhost:8080/images/"

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : MovieRepository {

    override suspend fun getAllMovies(): Result<List<Movie>> {
        return try {
            val movies = movieApi.getAllMovies().map { it.toDomain() }
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieById(id: Int): Result<Movie> {
        return try {
            val movie = movieApi.getMovieById(id).toDomain()
            Result.success(movie)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun MovieResponseDto.toDomain(): Movie {
        return Movie(
            id = id,
            title = title,
            description = description,
            duration = duration,
            genre = genre,
            director = director,
            actors = actors,
            language = language,
            rated = rated,
            releaseDate = releaseDate?.let {
                try {
                    LocalDate.parse(it)
                } catch (e: Exception) {
                    null
                }
            },
            posterUrl = poster?.let { IMAGE_BASE_URL + it },
            trailer = trailer,
            isActive = status ?: true
        )
    }
}