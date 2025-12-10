package com.michambita.ui.components.auth.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp

@Composable
fun RegistroPaso2(
    empresaOption: String,
    empresaNombre: String,
    empresaCodigo: String,
    onEmpresaOptionChange: (String) -> Unit,
    onEmpresaNombreChange: (String) -> Unit,
    onEmpresaCodigoChange: (String) -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    val step2Valid = if (empresaOption == "crear") {
        empresaNombre.isNotBlank()
    } else {
        empresaCodigo.isNotBlank()
    }

    var empresaNombreWasFocused by remember { mutableStateOf(false) }
    var empresaCodigoWasFocused by remember { mutableStateOf(false) }
    var empresaNombreShowError by remember { mutableStateOf(false) }
    var empresaCodigoShowError by remember { mutableStateOf(false) }

    Text(
        text = "Configuración de Empresa",
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Radio buttons for empresa option
    Column {
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = empresaOption == "crear",
                onClick = { onEmpresaOptionChange("crear") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Crear nueva empresa")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = empresaOption == "asociar",
                onClick = { onEmpresaOptionChange("asociar") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Asociarse a empresa existente")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Show appropriate field based on selection
    if (empresaOption == "crear") {
        OutlinedTextField(
            value = empresaNombre,
            onValueChange = onEmpresaNombreChange,
            label = { Text("Nombre de la empresa *") },
            isError = empresaNombreShowError && empresaNombre.isBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { f ->
                    if (f.isFocused) {
                        empresaNombreWasFocused = true
                    } else if (empresaNombreWasFocused) {
                        empresaNombreShowError = true
                    }
                }
        )
    } else {
        OutlinedTextField(
            value = empresaCodigo,
            onValueChange = onEmpresaCodigoChange,
            label = { Text("Código de empresa *") },
            isError = empresaCodigoShowError && empresaCodigo.isBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { f ->
                    if (f.isFocused) {
                        empresaCodigoWasFocused = true
                    } else if (empresaCodigoWasFocused) {
                        empresaCodigoShowError = true
                    }
                }
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Navigation buttons
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(1f)
        ) {
            Text("Atrás")
        }

        Button(
            onClick = onSubmit,
            enabled = step2Valid,
            modifier = Modifier.weight(1f)
        ) {
            Text("Registrarse")
        }
    }
}