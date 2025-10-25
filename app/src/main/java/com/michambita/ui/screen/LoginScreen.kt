package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.michambita.ui.viewmodel.AuthViewModel
import com.michambita.ui.common.UiState
import com.michambita.ui.components.widget.AlertModal
import com.michambita.ui.components.widget.ErrorDisplay
import com.michambita.ui.components.widget.LoadingOverlay
import com.michambita.utils.DismissKeyboardWrapper

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Iniciar Sesión" else "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!isLogin) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (!isLogin) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLogin) "Iniciar Sesión" else "Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isLogin = !isLogin }) {
            Text(if (isLogin) "¿No tienes cuenta? Regístrate" else "Ya tengo una cuenta")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    when (val state = uiState) {
        is UiState.Empty -> {}
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingOverlay("Cargando...")
            }
        }

        is UiState.Success -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AlertModal(
                    title = state.data,
                    message = "",
                    showDismissButton = false,
                    onConfirm = {
                        onLoginSuccess()
                    },
                    onDismissRequest = { }
                )
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorDisplay(
                    errorMessage = state.message,
                    onDismiss = {}
                )
            }
        }
    }
}
