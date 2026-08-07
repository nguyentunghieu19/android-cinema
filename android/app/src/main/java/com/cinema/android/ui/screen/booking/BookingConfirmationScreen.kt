package com.cinema.android.ui.screen.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF0F2F5)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF673AB7), Color(0xFFE91E63))
                    )
                )
        ) {
            when (val currentSeatState = seatState) {
                is SeatSelectionUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                is SeatSelectionUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = currentSeatState.message, color = Color.White)
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
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ConfirmationNumber,
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp),
                                    tint = Color(0xFFE91E63)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = "XÁC NHẬN ĐẶT VÉ",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp
                                    ),
                                    color = Color(0xFF3F51B5)
                                )
                                
                                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), thickness = 1.dp, color = Color.LightGray)

                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = showtime.movieTitle,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "🎬 ${showtime.cinemaName} - ${showtime.roomName}", color = Color.DarkGray)
                                    Text(text = "⏰ ${showtime.startTime.format(timeFormatter)}", color = Color.DarkGray)
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Surface(
                                        color = Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                                Text(text = "Ghế đã chọn:", style = MaterialTheme.typography.bodyMedium)
                                                Text(
                                                    text = selectedSeats.joinToString(", ") { "${it.row}${it.number}" },
                                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                    color = Color(0xFFE91E63)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                                Text(text = "Số lượng:", style = MaterialTheme.typography.bodyMedium)
                                                Text(text = "${selectedSeats.size} vé", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                            }
                                        }
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), thickness = 1.dp, color = Color.LightGray)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "TỔNG CỘNG", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                    Text(
                                        text = "%,.0f đ".format(totalPrice),
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            color = Color(0xFFE91E63),
                                            fontWeight = FontWeight.Black
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                when (bookingState) {
                                    is CreateBookingUiState.Loading -> {
                                        CircularProgressIndicator(color = Color(0xFFE91E63))
                                    }
                                    is CreateBookingUiState.Error -> {
                                        Text(
                                            text = (bookingState as CreateBookingUiState.Error).message,
                                            color = Color.Red,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
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
            }
        }
    }
}

@Composable
private fun ConfirmButton(showtimeId: Int, seatIds: List<Int>, viewModel: BookingViewModel) {
    Button(
        onClick = { viewModel.createBooking(showtimeId, seatIds, "VNPAY") },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
    ) {
        Text("THANH TOÁN NGAY", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
