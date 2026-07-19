package com.michambita.feature.auth.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.common.UiState
import com.michambita.core.common.util.ValidateUtil.isEmailValid
import com.michambita.feature.auth.components.login.LoginForm
import com.michambita.feature.auth.components.login.ResetPasswordDialog
import com.michambita.ui.components.widget.AlertModal
import com.michambita.ui.components.widget.ErrorDisplay
import com.michambita.ui.components.widget.LoadingOverlay
import com.michambita.feature.auth.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistro: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resetPasswordState by viewModel.resetPasswordState.collectAsStateWithLifecycle()

    var showResetPasswordDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }

    LaunchedEffect(resetPasswordState) {
        if (resetPasswordState is UiState.Success) {
            showResetPasswordDialog = false
            viewModel.clearResetState()
        }
    }

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

        TextButton(onClick = {
            resetEmail = authUiState.email
            showResetPasswordDialog = true
        }) {
            Text("¿Olvidaste tu contraseña?")
        }
        TextButton(onClick = onNavigateToRegistro) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }

    if (showResetPasswordDialog) {
        ResetPasswordDialog(
            email = resetEmail,
            onEmailChange = { resetEmail = it },
            isEmailValid = isEmailValid(resetEmail),
            isLoading = resetPasswordState is UiState.Loading,
            errorMessage = (resetPasswordState as? UiState.Error)?.message,
            onConfirm = { viewModel.sendResetPassword(resetEmail) },
            onDismiss = {
                showResetPasswordDialog = false
                viewModel.clearResetState()
            }
        )
    }

    AuthStateOverlay(
        state = uiState,
        onSuccess = onLoginSuccess,
        onDismissError = viewModel::clearError,
    )
}

@Composable
private fun AuthStateOverlay(
    state: UiState<String>,
    onSuccess: () -> Unit,
    onDismissError: () -> Unit,
) {
    when (state) {
        is UiState.Empty -> {}
        is UiState.Loading -> LoadingOverlay(modifier = Modifier, message = "Cargando...")
        is UiState.Success -> AlertModal(
            modifier = Modifier,
            title = state.data,
            message = "",
            showDismissButton = false,
            onConfirm = onSuccess,
            onDismissRequest = { },
        )
        is UiState.Error -> ErrorDisplay(
            modifier = Modifier,
            errorMessage = state.message,
            onDismiss = onDismissError,
        )
    }
}
