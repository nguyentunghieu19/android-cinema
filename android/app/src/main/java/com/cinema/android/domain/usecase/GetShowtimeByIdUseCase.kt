package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.Showtime
import com.cinema.android.domain.repository.ShowtimeRepository
import javax.inject.Inject

class GetShowtimeByIdUseCase @Inject constructor(
    private val showtimeRepository: ShowtimeRepository
) {
    suspend operator fun invoke(showtimeId: Int): Result<Showtime> {
        return showtimeRepository.getShowtimeById(showtimeId)
    }
}