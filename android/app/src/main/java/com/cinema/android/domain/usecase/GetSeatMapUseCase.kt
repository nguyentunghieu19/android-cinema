// GetSeatMapUseCase.kt
package com.cinema.android.domain.usecase

import com.cinema.android.domain.model.Seat
import com.cinema.android.domain.repository.ShowtimeRepository
import javax.inject.Inject

class GetSeatMapUseCase @Inject constructor(
    private val showtimeRepository: ShowtimeRepository
) {
    suspend operator fun invoke(showtimeId: Int): Result<List<Seat>> {
        return showtimeRepository.getSeatMap(showtimeId)
    }
}