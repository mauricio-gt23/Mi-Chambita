package com.michambita.ui.components.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val emailError = email.isBlank()
    val passwordError = password.isBlank()
    val formValid = !emailError && !passwordError

    var emailWasFocused by remember { mutableStateOf(false) }
    var passwordWasFocused by remember { mutableStateOf(false) }
    var emailShowError by remember { mutableStateOf(false) }
    var passwordShowError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Correo electrónico *") },
        isError = emailShowError && emailError,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { f ->
                if (f.isFocused) {
                    emailWasFocused = true
                } else if (emailWasFocused) {
                    emailShowError = true
                }
            }
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Contraseña *") },
        visualTransformation = PasswordVisualTransformation(),
        isError = passwordShowError && passwordError,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { f ->
                if (f.isFocused) {
                    passwordWasFocused = true
                } else if (passwordWasFocused) {
                    passwordShowError = true
                }
            }
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onSubmit,
        enabled = formValid,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Iniciar Sesión")
    }
}