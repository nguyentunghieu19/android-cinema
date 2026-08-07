package com.cinema.android.ui.screen.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cinema.android.domain.model.Showtime
import com.cinema.android.ui.viewmodel.BookingViewModel
import com.cinema.android.ui.viewmodel.ShowtimeListUiState
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                    )
                )
        ) {
            when (val currentState = state) {
                is ShowtimeListUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFFC107))
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
                            Text(
                                text = "Chưa có suất chiếu nào cho phim này 🍿",
                                color = Color.Gray,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text(
                                    text = "Chọn suất chiếu",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Black
                                    ),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            currentState.showtimesByCinema.forEach { (cinemaName, showtimes) ->
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = cinemaName,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    color = Color(0xFFFFC107),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            FlowRow(
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                verticalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                showtimes.forEach { showtime ->
                                                    ShowtimeChip(
                                                        showtime = showtime,
                                                        onClick = { onShowtimeSelected(showtime.id) }
                                                    )
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
        }
    }
}

@Composable
private fun ShowtimeChip(showtime: Showtime, onClick: () -> Unit) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(1.dp, Color(0xFF444444), RoundedCornerShape(8.dp)),
        color = Color(0xFF333333),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = showtime.startTime.format(timeFormatter),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "%,.0f đ".format(showtime.price),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}
