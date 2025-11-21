package com.michambita.ui.components.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun AuthForm(
    isLogin: Boolean,
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onToggleMode: () -> Unit
) {
    val emailError = email.isBlank()
    val passwordError = password.isBlank()
    val nameError = !isLogin && name.isBlank()
    val confirmError = !isLogin && confirmPassword.isBlank()
    val formValid = if (isLogin) !emailError && !passwordError else !nameError && !emailError && !passwordError && !confirmError

    var emailWasFocused by remember(isLogin) { mutableStateOf(false) }
    var passwordWasFocused by remember(isLogin) { mutableStateOf(false) }
    var nameWasFocused by remember(isLogin) { mutableStateOf(false) }
    var confirmWasFocused by remember(isLogin) { mutableStateOf(false) }

    var emailShowError by remember(isLogin) { mutableStateOf(false) }
    var passwordShowError by remember(isLogin) { mutableStateOf(false) }
    var nameShowError by remember(isLogin) { mutableStateOf(false) }
    var confirmShowError by remember(isLogin) { mutableStateOf(false) }
    
    Text(
        text = if (isLogin) "Iniciar Sesión" else "Crear Cuenta",
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(modifier = Modifier.height(24.dp))

    if (!isLogin) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre completo *") },
            isError = nameShowError && nameError,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { f ->
                    if (f.isFocused) {
                        nameWasFocused = true
                    } else if (nameWasFocused) {
                        nameShowError = true
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

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

    if (!isLogin) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirmar contraseña *") },
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmShowError && confirmError,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { f ->
                    if (f.isFocused) {
                        confirmWasFocused = true
                    } else if (confirmWasFocused) {
                        confirmShowError = true
                    }
                }
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onSubmit,
        enabled = formValid,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(if (isLogin) "Iniciar Sesión" else "Registrarse")
    }

    Spacer(modifier = Modifier.height(16.dp))

    TextButton(onClick = onToggleMode) {
        Text(if (isLogin) "¿No tienes cuenta? Regístrate" else "Ya tengo una cuenta")
    }
}