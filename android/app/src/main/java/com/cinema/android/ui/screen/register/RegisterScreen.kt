package com.cinema.android.ui.screen.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cinema.android.domain.model.User
import com.cinema.android.ui.viewmodel.AuthViewModel
import com.cinema.android.ui.viewmodel.RegisterUiState

@Composable
fun RegisterScreen(
    onRegisterSuccess: (User) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val registerState by viewModel.registerState.collectAsStateWithLifecycle()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }

    LaunchedEffect(registerState) {
        if (registerState is RegisterUiState.Success) {
            onRegisterSuccess((registerState as RegisterUiState.Success).user)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Đăng ký", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        val enabled = registerState !is RegisterUiState.Loading

        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Tên đăng nhập *") }, modifier = Modifier.fillMaxWidth(), enabled = enabled
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Mật khẩu *") }, modifier = Modifier.fillMaxWidth(), enabled = enabled
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = fullname, onValueChange = { fullname = it },
            label = { Text("Họ tên") }, modifier = Modifier.fillMaxWidth(), enabled = enabled
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), enabled = enabled
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone, onValueChange = { phone = it },
            label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth(), enabled = enabled
        )
        Spacer(modifier = Modifier.height(20.dp))

        when (registerState) {
            is RegisterUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is RegisterUiState.Error -> {
                Text(
                    text = (registerState as RegisterUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
                RegisterButton(username, password, email, phone, fullname, viewModel)
            }
            else -> {
                RegisterButton(username, password, email, phone, fullname, viewModel)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Đã có tài khoản? Đăng nhập")
        }
    }
}

@Composable
private fun RegisterButton(
    username: String,
    password: String,
    email: String,
    phone: String,
    fullname: String,
    viewModel: AuthViewModel
) {
    Button(
        onClick = {
            viewModel.register(
                username = username,
                password = password,
                email = email.ifBlank { null },
                phone = phone.ifBlank { null },
                fullname = fullname.ifBlank { null }
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Đăng ký")
    }
}