package com.michambita.feature.auth.components.registro

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.core.common.util.ValidateUtil.isEmailValid
import com.michambita.ui.components.widget.RequiredTextField
import com.michambita.ui.components.widget.PasswordTextField
import com.michambita.ui.components.widget.PasswordStrengthIndicator

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
    val emailInvalid = email.isNotBlank() && !isEmailValid(email)
    val emailCustomError = if (emailInvalid) "Formato de correo inválido" else null
    
    val passwordMismatch = password != confirmPassword
    val confirmCustomError = if (passwordMismatch && confirmPassword.isNotEmpty()) {
        "Las contraseñas no coinciden"
    } else null

    val hasMinLength = password.length >= 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { it in "!@#$%^&*()_+-=[]{}|;':\",./<>?~`" }
    val passwordValid = hasMinLength && hasUppercase && hasDigit && hasSpecialChar

    val formValid = name.isNotBlank() && 
            email.isNotBlank() && !emailInvalid && 
            passwordValid && 
            confirmPassword.isNotBlank() && !passwordMismatch

    var isPasswordFocused by remember { mutableStateOf(false) }

    RequiredTextField(
        value = name,
        onValueChange = onNameChange,
        label = "Nombre completo",
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    RequiredTextField(
        value = email,
        onValueChange = onEmailChange,
        label = "Correo electrónico",
        customError = emailCustomError,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    PasswordTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = "Contraseña",
        onFocusChanged = { isPasswordFocused = it },
        modifier = Modifier.fillMaxWidth()
    )

    PasswordStrengthIndicator(
        isVisible = isPasswordFocused,
        hasMinLength = hasMinLength,
        hasUppercase = hasUppercase,
        hasDigit = hasDigit,
        hasSpecialChar = hasSpecialChar,
        allRulesMet = passwordValid,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    PasswordTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = "Confirmar contraseña",
        customError = confirmCustomError,
        modifier = Modifier.fillMaxWidth()
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