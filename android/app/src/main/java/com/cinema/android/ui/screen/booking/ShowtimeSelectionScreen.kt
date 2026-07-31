package com.cinema.android.ui.screen.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.cinema.android.domain.model.Showtime
import com.cinema.android.ui.viewmodel.BookingViewModel
import com.cinema.android.ui.viewmodel.ShowtimeListUiState
import java.time.format.DateTimeFormatter

@Composable
fun ShowtimeSelectionScreen(
    movieId: Int,
    onShowtimeSelected: (Int) -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val state by viewModel.showtimeListState.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        viewModel.loadShowtimes(movieId)
    }

    when (val currentState = state) {
        is ShowtimeListUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ShowtimeListUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is ShowtimeListUiState.Success -> {
            if (currentState.showtimesByCinema.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Chua co suat chieu nao cho phim nay")
                }
                return
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
            ) {
                currentState.showtimesByCinema.forEach { (cinemaName, showtimes) ->
                    item {
                        Text(
                            text = cinemaName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                        )
                    }
                    item {
                        androidx.compose.foundation.layout.FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            showtimes.forEach { showtime ->
                                ShowtimeChip(
                                    showtime = showtime,
                                    onClick = { onShowtimeSelected(showtime.id) }
                                )
                            }
                        }
                    }
                    item {
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowtimeChip(showtime: Showtime, onClick: () -> Unit) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = showtime.startTime.format(timeFormatter),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}