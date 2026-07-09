package com.michambita.feature.auth.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.core.common.util.ValidateUtil.isEmailValid
import com.michambita.ui.components.widget.RequiredTextField
import com.michambita.ui.components.widget.PasswordTextField

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val emailInvalid = email.isNotBlank() && !isEmailValid(email)
    val emailCustomError = if (emailInvalid) "Formato de correo inválido" else null
    val formValid = email.isNotBlank() && !emailInvalid && password.isNotBlank()

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
        modifier = Modifier.fillMaxWidth()
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