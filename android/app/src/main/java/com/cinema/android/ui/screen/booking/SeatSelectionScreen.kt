package com.cinema.android.ui.screen.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cinema.android.domain.model.Seat
import com.cinema.android.domain.model.SeatStatus
import com.cinema.android.ui.viewmodel.BookingViewModel
import com.cinema.android.ui.viewmodel.SeatSelectionUiState

@Composable
fun SeatSelectionScreen(
    showtimeId: Int,
    onConfirm: (showtimeId: Int, seatIds: List<Int>) -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val state by viewModel.seatSelectionState.collectAsStateWithLifecycle()
    var selectedSeatIds by remember { mutableStateOf(setOf<Int>()) }

    LaunchedEffect(showtimeId) {
        viewModel.loadSeatSelectionData(showtimeId)
    }

    when (val currentState = state) {
        is SeatSelectionUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is SeatSelectionUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is SeatSelectionUiState.Success -> {
            val showtime = currentState.showtime
            val seatsByRow = currentState.seats.groupBy { it.row }.toSortedMap()
            val totalPrice = selectedSeatIds.size * showtime.price

            Column(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = showtime.movieTitle, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "${showtime.cinemaName} - ${showtime.roomName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    seatsByRow.forEach { (row, seatsInRow) ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            seatsInRow.sortedBy { it.number }.forEach { seat ->
                                SeatBox(
                                    seat = seat,
                                    isSelected = selectedSeatIds.contains(seat.id),
                                    onClick = {
                                        if (seat.status == SeatStatus.AVAILABLE) {
                                            selectedSeatIds = if (selectedSeatIds.contains(seat.id)) {
                                                selectedSeatIds - seat.id
                                            } else {
                                                selectedSeatIds + seat.id
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SeatLegend()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Da chon: ${selectedSeatIds.size} ghe")
                    Text(
                        text = "Tong tien: %,.0f d".format(totalPrice),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onConfirm(showtimeId, selectedSeatIds.toList()) },
                        enabled = selectedSeatIds.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Xac nhan")
                    }
                }
            }
        }
    }
}

@Composable
private fun SeatBox(seat: Seat, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        seat.status == SeatStatus.BOOKED -> MaterialTheme.colorScheme.surfaceVariant
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick, enabled = seat.status == SeatStatus.AVAILABLE),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${seat.row}${seat.number}",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun SeatLegend() {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        LegendItem(color = MaterialTheme.colorScheme.secondaryContainer, label = "Trong")
        LegendItem(color = MaterialTheme.colorScheme.primary, label = "Da chon")
        LegendItem(color = MaterialTheme.colorScheme.surfaceVariant, label = "Da dat")
    }
}

@Composable
private fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}