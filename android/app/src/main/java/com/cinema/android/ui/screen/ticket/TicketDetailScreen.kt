package com.cinema.android.ui.screen.ticket

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cinema.android.domain.model.BookingStatus
import com.cinema.android.ui.viewmodel.TicketDetailUiState
import com.cinema.android.ui.viewmodel.TicketDetailViewModel
import java.time.format.DateTimeFormatter

@Composable
fun TicketDetailScreen(
    bookingId: Int,
    onBack: () -> Unit,
    viewModel: TicketDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(bookingId) {
        viewModel.loadDetail(bookingId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF121212),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Quay lại")
                }
                Text(
                    text = "Chi tiết vé phim",
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1A237E), Color(0xFF000000))
                    )
                )
        ) {
            when (val currentState = state) {
                is TicketDetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFFC107))
                    }
                }

                is TicketDetailUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = currentState.message, color = Color.White)
                    }
                }

                is TicketDetailUiState.Success -> {
                    val booking = currentState.booking
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")
                    val (statusText, statusColor) = when (booking.bookingStatus) {
                        BookingStatus.PAID -> "ĐÃ THANH TOÁN" to Color(0xFF4CAF50)
                        BookingStatus.PENDING -> "CHỜ THANH TOÁN" to Color(0xFFFF9800)
                        BookingStatus.CANCELLED -> "ĐÃ HỦY" to Color(0xFFF44336)
                        BookingStatus.UNKNOWN -> "KHÔNG XÁC ĐỊNH" to Color.Gray
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Ticket Header (Movie Info)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFFFC107))
                                        .padding(20.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color.Black)
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(
                                            text = booking.bookingCode,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Black,
                                            color = Color.Black
                                        )
                                    }
                                }

                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = booking.movieName,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.Black
                                        )
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    DetailItem("Rạp chiếu", "${booking.cinemaName} - ${booking.roomName}")
                                    DetailItem("Thời gian", booking.showtime.format(timeFormatter))
                                    DetailItem("Vị trí ghế", booking.seats.joinToString(", "))
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Dotted Line
                                    DashedDivider()
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Tổng cộng", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                            Text(
                                                text = "%,.0f đ".format(booking.totalAmount),
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFFE91E63)
                                                )
                                            )
                                        }
                                        Surface(
                                            color = statusColor.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = statusText,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = statusColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))
                                    
                                    // Simulated QR Code
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            Icons.Default.QrCode2,
                                            contentDescription = null,
                                            modifier = Modifier.size(120.dp),
                                            tint = Color.Black
                                        )
                                        Text(
                                            text = "Quét mã để vào phòng chiếu",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Vui lòng có mặt tại quầy vé trước 15 phút nếu cần hỗ trợ in vé vật lý.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold), color = Color.Black)
    }
}

@Composable
private fun DashedDivider() {
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = Color.LightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}
