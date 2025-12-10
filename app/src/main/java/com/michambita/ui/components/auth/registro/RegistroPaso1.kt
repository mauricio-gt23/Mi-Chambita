package com.michambita.ui.components.auth.registro

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegistroPaso1(
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    val nameError = name.isBlank()
    val emailError = email.isBlank()
    val passwordError = password.isBlank()
    val confirmError = confirmPassword.isBlank()
    val passwordMismatch = password != confirmPassword

    val formValid = !nameError && !emailError && !passwordError && !confirmError && !passwordMismatch

    var nameWasFocused by remember { mutableStateOf(false) }
    var emailWasFocused by remember { mutableStateOf(false) }
    var passwordWasFocused by remember { mutableStateOf(false) }
    var confirmWasFocused by remember { mutableStateOf(false) }

    var nameShowError by remember { mutableStateOf(false) }
    var emailShowError by remember { mutableStateOf(false) }
    var passwordShowError by remember { mutableStateOf(false) }
    var confirmShowError by remember { mutableStateOf(false) }

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

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = { Text("Confirmar contraseña *") },
        visualTransformation = PasswordVisualTransformation(),
        isError = confirmShowError && (confirmError || passwordMismatch),
        supportingText = if (confirmShowError && passwordMismatch && !confirmError) {
            { Text("Las contraseñas no coinciden") }
        } else null,
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

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onContinue,
        enabled = formValid,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Continuar")
    }
}