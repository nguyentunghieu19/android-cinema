// GetShowtimesByMovieUseCase.kt
package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.Showtime
import com.cinema.android.domain.repository.ShowtimeRepository
import javax.inject.Inject

class GetShowtimesByMovieUseCase @Inject constructor(
    private val showtimeRepository: ShowtimeRepository
) {
    suspend operator fun invoke(movieId: Int): Result<List<Showtime>> {
        return showtimeRepository.getShowtimesByMovie(movieId)
    }
}