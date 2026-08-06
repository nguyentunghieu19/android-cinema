package com.cinema.android.ui.screen.booking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cinema.android.ui.viewmodel.BookingViewModel
import com.cinema.android.ui.viewmodel.CreateBookingUiState
import com.cinema.android.ui.viewmodel.SeatSelectionUiState
import java.time.format.DateTimeFormatter

@Composable
fun BookingConfirmationScreen(
    showtimeId: Int,
    seatIds: List<Int>,
    onBookingSuccess: (bookingId: Int, bookingCode: String, totalAmount: Double) -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val seatState by viewModel.seatSelectionState.collectAsStateWithLifecycle()
    val bookingState by viewModel.createBookingState.collectAsStateWithLifecycle()

    LaunchedEffect(showtimeId) {
        viewModel.loadSeatSelectionData(showtimeId)
    }

    LaunchedEffect(bookingState) {
        if (bookingState is CreateBookingUiState.Success) {
            val result = (bookingState as CreateBookingUiState.Success).result
            onBookingSuccess(result.bookingId, result.bookingCode, result.totalAmount)
        }
    }

    when (val currentSeatState = seatState) {
        is SeatSelectionUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is SeatSelectionUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentSeatState.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is SeatSelectionUiState.Success -> {
            val showtime = currentSeatState.showtime
            val selectedSeats = currentSeatState.seats.filter { seatIds.contains(it.id) }
            val totalPrice = selectedSeats.size * showtime.price
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Xac nhan dat ve", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = showtime.movieTitle, style = MaterialTheme.typography.titleMedium)
                Text(text = "${showtime.cinemaName} - ${showtime.roomName}")
                Text(text = showtime.startTime.format(timeFormatter))

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Text(
                    text = "Ghe: " + selectedSeats.joinToString(", ") { "${it.row}${it.number}" }
                )
                Text(text = "So luong: ${selectedSeats.size} ghe")

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Text(
                    text = "Tong tien: %,.0f d".format(totalPrice),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                when (bookingState) {
                    is CreateBookingUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is CreateBookingUiState.Error -> {
                        Text(
                            text = (bookingState as CreateBookingUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ConfirmButton(showtimeId, seatIds, viewModel)
                    }
                    else -> {
                        ConfirmButton(showtimeId, seatIds, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfirmButton(showtimeId: Int, seatIds: List<Int>, viewModel: BookingViewModel) {
    Button(
        onClick = { viewModel.createBooking(showtimeId, seatIds, "VNPAY") },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Xac nhan dat ve")
    }
}