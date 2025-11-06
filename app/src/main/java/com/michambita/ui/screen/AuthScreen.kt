package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.michambita.ui.viewmodel.AuthViewModel
import com.michambita.ui.common.UiState
import com.michambita.ui.components.auth.AuthForm
import com.michambita.ui.components.widget.AlertModal
import com.michambita.ui.components.widget.ErrorDisplay
import com.michambita.ui.components.widget.LoadingOverlay

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLogin by remember { mutableStateOf(true) }

    var showError by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthForm(
            isLogin = isLogin,
            name = name,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            onNameChange = { name = it },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onConfirmPasswordChange = { confirmPassword = it },
            onSubmit = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    if (isLogin) {
                        viewModel.login(email, password)
                    } else {
                        if (password == confirmPassword) {
                            viewModel.register(name, email, password)
                        } else {
                            // TODO: Mostrar mensaje de error con ErrorDisplay
                            // viewModel.setError("Las contraseñas no coinciden")
                        }
                    }
                }
            },
            onToggleMode = { isLogin = !isLogin }
        )
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
                isVisible = showError,
                onDismiss = { showError = false }
            )
        }
    }
}
