package com.cinema.android.ui.screen.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cinema.android.domain.model.BookingStatus
import com.cinema.android.domain.model.MyBooking
import com.cinema.android.ui.viewmodel.BookingHistoryUiState
import com.cinema.android.ui.viewmodel.ProfileViewModel
import java.time.format.DateTimeFormatter

@Composable
fun TicketHistoryScreen(
    onBookingClick: (Int) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.historyState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadBookingHistory()
    }

    when (val currentState = state) {
        is BookingHistoryUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is BookingHistoryUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is BookingHistoryUiState.Success -> {
            if (currentState.bookings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Ban chua dat ve nao")
                }
                return
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
            ) {
                items(currentState.bookings) { booking ->
                    BookingHistoryItem(
                        booking = booking,
                        onClick = { onBookingClick(booking.bookingId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingHistoryItem(booking: MyBooking, onClick: () -> Unit) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")
    val statusText = when (booking.status) {
        BookingStatus.PAID -> "Da thanh toan"
        BookingStatus.PENDING -> "Cho thanh toan"
        BookingStatus.CANCELLED -> "Da huy"
        BookingStatus.UNKNOWN -> "Khong xac dinh"
    }
    val statusColor = when (booking.status) {
        BookingStatus.PAID -> MaterialTheme.colorScheme.primary
        BookingStatus.CANCELLED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp)
    ) {
        Text(text = booking.movieName, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "${booking.cinemaName} - ${booking.roomName}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(text = booking.showtime.format(timeFormatter), style = MaterialTheme.typography.bodySmall)
        Text(
            text = "%,.0f d".format(booking.totalAmount),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(text = statusText, style = MaterialTheme.typography.labelMedium, color = statusColor)
    }
}