package com.cinema.android.ui.screen.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF5F5F5) // Light gray background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFE0B2), // Light Orange
                            Color(0xFFE1F5FE)  // Light Blue
                        )
                    )
                )
        ) {
            when (val currentState = state) {
                is BookingHistoryUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFF5722))
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
                            Text(
                                text = "Bạn chưa đặt vé nào 🍿",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
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
        }
    }
}

@Composable
private fun BookingHistoryItem(booking: MyBooking, onClick: () -> Unit) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")
    val statusText = when (booking.status) {
        BookingStatus.PAID -> "Đã thanh toán"
        BookingStatus.PENDING -> "Chờ thanh toán"
        BookingStatus.CANCELLED -> "Đã hủy"
        BookingStatus.UNKNOWN -> "Không xác định"
    }
    val statusColor = when (booking.status) {
        BookingStatus.PAID -> Color(0xFF4CAF50)
        BookingStatus.PENDING -> Color(0xFFFF9800)
        BookingStatus.CANCELLED -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = booking.movieName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${booking.cinemaName} • ${booking.roomName}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Text(
                text = booking.showtime.format(timeFormatter),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "%,.0f đ".format(booking.totalAmount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFFE91E63),
                        fontWeight = FontWeight.Black
                    ),
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
