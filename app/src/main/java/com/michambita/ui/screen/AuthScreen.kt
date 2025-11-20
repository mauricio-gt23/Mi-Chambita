package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val authUiSate by viewModel.authUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showError by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthForm(
            isLogin = authUiSate.isLogin,
            name = authUiSate.name,
            email = authUiSate.email,
            password = authUiSate.password,
            confirmPassword = authUiSate.confirmPassword,
            onNameChange = viewModel::updateNombre ,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onConfirmPasswordChange = viewModel::updateConfirmPassword,
            onSubmit = { viewModel.submit() },
            onToggleMode = viewModel::updateIsLogin
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
