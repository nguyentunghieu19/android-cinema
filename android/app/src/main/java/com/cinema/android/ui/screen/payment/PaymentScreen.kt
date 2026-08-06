package com.cinema.android.ui.screen.payment

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cinema.android.ui.viewmodel.PaymentUiState
import com.cinema.android.ui.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    bookingId: Int,
    onPaymentSuccess: () -> Unit,
    onPaymentFailed: (String) -> Unit,
    onCancel: () -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(bookingId) {
        viewModel.createPayment(bookingId)
    }

    LaunchedEffect(state) {
        val currentState = state
        if (currentState is PaymentUiState.ReadyToOpenBrowser) {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(context, Uri.parse(currentState.paymentUrl))
            viewModel.onBrowserOpened()
        }
        if (currentState is PaymentUiState.Success) {
            onPaymentSuccess()
        }
        if (currentState is PaymentUiState.Failed) {
            onPaymentFailed(currentState.message)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (state is PaymentUiState.WaitingForUser || state is PaymentUiState.Pending) {
            viewModel.checkBookingStatus(bookingId)
        }
    }

    when (val currentState = state) {
        is PaymentUiState.CreatingPayment, is PaymentUiState.ReadyToOpenBrowser -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is PaymentUiState.WaitingForUser, is PaymentUiState.CheckingStatus -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Dang cho xac nhan thanh toan...",
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Hoan tat thanh toan tren trinh duyet, sau do quay lai app.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedButton(
                    onClick = { viewModel.checkBookingStatus(bookingId) },
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("Toi da thanh toan xong - Kiem tra lai")
                }
            }
        }

        is PaymentUiState.Pending -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Don hang chua duoc thanh toan.")
                OutlinedButton(
                    onClick = { viewModel.checkBookingStatus(bookingId) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Kiem tra lai")
                }
            }
        }

        is PaymentUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                Button(onClick = onCancel, modifier = Modifier.padding(top = 16.dp).fillMaxWidth()) {
                    Text("Quay lai")
                }
            }
        }

        else -> Unit
    }
}