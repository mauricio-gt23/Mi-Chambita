package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.ui.viewmodel.RegistroViewModel
import com.michambita.ui.common.UiState
import com.michambita.ui.components.auth.registro.RegistroPaso1
import com.michambita.ui.components.auth.registro.RegistroPaso2
import com.michambita.ui.components.widget.AlertModal
import com.michambita.ui.components.widget.ErrorDisplay
import com.michambita.ui.components.widget.LoadingOverlay

@Composable
fun RegistroScreen(
    onRegistroSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegistroViewModel = hiltViewModel()
) {
    val registroUiState by viewModel.registroUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showError by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title with step indicator
        Column {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Paso ${registroUiState.currentStep} de 2",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Multi-step registration form
        when (registroUiState.currentStep) {
            1 -> {
                RegistroPaso1(
                    name = registroUiState.name,
                    email = registroUiState.email,
                    password = registroUiState.password,
                    confirmPassword = registroUiState.confirmPassword,
                    onNameChange = viewModel::updateNombre,
                    onEmailChange = viewModel::updateEmail,
                    onPasswordChange = viewModel::updatePassword,
                    onConfirmPasswordChange = viewModel::updateConfirmPassword,
                    onContinue = { viewModel.updateCurrentStep(2) }
                )
            }
            2 -> {
                RegistroPaso2(
                    empresaOption = registroUiState.empresaOption,
                    empresaNombre = registroUiState.empresaNombre,
                    empresaCodigo = registroUiState.empresaCodigo,
                    onEmpresaOptionChange = viewModel::updateEmpresaOption,
                    onEmpresaNombreChange = viewModel::updateEmpresaNombre,
                    onEmpresaCodigoChange = viewModel::updateEmpresaCodigo,
                    onBack = { viewModel.updateCurrentStep(1) },
                    onSubmit = { viewModel.register(
                        registroUiState.name,
                        registroUiState.email,
                        registroUiState.password
                    ) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Ya tengo una cuenta")
        }
    }

    // Handle UI states
    when (val state = uiState) {
        is UiState.Empty -> {}
        is UiState.Loading -> {
            LoadingOverlay(modifier = Modifier, message = "Registrando...")
        }
        is UiState.Success -> {
            AlertModal(
                modifier = Modifier,
                title = state.data,
                message = "",
                showDismissButton = false,
                onConfirm = {
                    onRegistroSuccess()
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
