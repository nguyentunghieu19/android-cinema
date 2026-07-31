package com.cinema.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinema.android.domain.usecase.CreateBookingUseCase
import com.cinema.android.domain.usecase.GetSeatMapUseCase
import com.cinema.android.domain.usecase.GetShowtimeByIdUseCase
import com.cinema.android.domain.usecase.GetShowtimesByMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getShowtimesByMovieUseCase: GetShowtimesByMovieUseCase,
    private val getShowtimeByIdUseCase: GetShowtimeByIdUseCase,
    private val getSeatMapUseCase: GetSeatMapUseCase,
    private val createBookingUseCase: CreateBookingUseCase
) : ViewModel() {

    private val _showtimeListState = MutableStateFlow<ShowtimeListUiState>(ShowtimeListUiState.Loading)
    val showtimeListState: StateFlow<ShowtimeListUiState> = _showtimeListState.asStateFlow()

    private val _seatSelectionState = MutableStateFlow<SeatSelectionUiState>(SeatSelectionUiState.Loading)
    val seatSelectionState: StateFlow<SeatSelectionUiState> = _seatSelectionState.asStateFlow()

    private val _createBookingState = MutableStateFlow<CreateBookingUiState>(CreateBookingUiState.Idle)
    val createBookingState: StateFlow<CreateBookingUiState> = _createBookingState.asStateFlow()

    fun loadShowtimes(movieId: Int) {
        viewModelScope.launch {
            _showtimeListState.value = ShowtimeListUiState.Loading
            val result = getShowtimesByMovieUseCase(movieId)
            _showtimeListState.value = result.fold(
                onSuccess = { list ->
                    val grouped = list.sortedBy { it.startTime }.groupBy { it.cinemaName }
                    ShowtimeListUiState.Success(grouped)
                },
                onFailure = { error ->
                    ShowtimeListUiState.Error(error.message ?: "Khong tai duoc suat chieu")
                }
            )
        }
    }

    fun loadSeatSelectionData(showtimeId: Int) {
        viewModelScope.launch {
            _seatSelectionState.value = SeatSelectionUiState.Loading

            val showtimeResult = getShowtimeByIdUseCase(showtimeId)
            val seatsResult = getSeatMapUseCase(showtimeId)

            _seatSelectionState.value = if (showtimeResult.isSuccess && seatsResult.isSuccess) {
                SeatSelectionUiState.Success(
                    showtime = showtimeResult.getOrThrow(),
                    seats = seatsResult.getOrThrow()
                )
            } else {
                val errorMessage = showtimeResult.exceptionOrNull()?.message
                    ?: seatsResult.exceptionOrNull()?.message
                    ?: "Khong tai duoc thong tin dat ve"
                SeatSelectionUiState.Error(errorMessage)
            }
        }
    }

    fun createBooking(showtimeId: Int, seatIds: List<Int>, paymentMethod: String) {
        viewModelScope.launch {
            _createBookingState.value = CreateBookingUiState.Loading
            val result = createBookingUseCase(showtimeId, seatIds, paymentMethod)
            _createBookingState.value = result.fold(
                onSuccess = { booking -> CreateBookingUiState.Success(booking) },
                onFailure = { error -> CreateBookingUiState.Error(error.message ?: "Dat ve that bai") }
            )
        }
    }
}