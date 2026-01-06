package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.ui.common.UiState
import com.michambita.ui.components.auth.login.LoginForm
import com.michambita.ui.components.widget.AlertModal
import com.michambita.ui.components.widget.ErrorDisplay
import com.michambita.ui.components.widget.LoadingOverlay
import com.michambita.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistro: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        LoginForm(
            email = authUiState.email,
            password = authUiState.password,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onSubmit = { 
                viewModel.login(authUiState.email, authUiState.password)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegistro) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }

    when (val state = uiState) {
        is UiState.Empty -> {}
        is UiState.Loading -> {
            LoadingOverlay(modifier = Modifier, message = "Cargando...")
        }
        is UiState.Success -> {
            AlertModal(
                modifier = Modifier,
                title = state.data,
                message = "",
                showDismissButton = false,
                onConfirm = {
                    onLoginSuccess()
                },
                onDismissRequest = { }
            )
        }
        is UiState.Error -> {
            ErrorDisplay(
                modifier = Modifier,
                errorMessage = state.message,
                onDismiss = { viewModel.clearError() }
            )
        }
    }
}
