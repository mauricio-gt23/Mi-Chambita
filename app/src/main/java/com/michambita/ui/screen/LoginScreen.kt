package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.michambita.domain.viewmodel.LoginViewModel
import com.michambita.ui.common.UiState
import com.michambita.utils.DismissKeyboardWrapper

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    DismissKeyboardWrapper {
        val uiState by viewModel.uiState.collectAsState()
        var email by remember { mutableStateOf("") }

        LaunchedEffect(uiState) {
            if (uiState is UiState.Success) { onLoginSuccess() }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        viewModel.loginConCorreo(email)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seguir")
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (uiState) {
                is UiState.Empty -> Text("Ingresa tu correo para continuar")
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> Text(text = (uiState as UiState.Success).data, color = Color.Green)
                is UiState.Error -> Text(text = "Error: ${(uiState as UiState.Error).message}", color = Color.Red)
            }
        }
    }
}