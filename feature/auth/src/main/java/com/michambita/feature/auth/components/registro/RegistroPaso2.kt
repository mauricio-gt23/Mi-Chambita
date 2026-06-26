package com.michambita.feature.auth.components.registro

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
    companyOption: String,
    companyName: String,
    companyCode: String,
    onCompanyOptionChange: (String) -> Unit,
    onCompanyNameChange: (String) -> Unit,
    onCompanyCodeChange: (String) -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    val step2Valid = if (companyOption == "crear") {
        companyName.isNotBlank()
    } else {
        companyCode.isNotBlank()
    }

    val submitLabel = if (companyOption == "crear") "Continuar" else "Registrarse"

    var companyNameWasFocused by remember { mutableStateOf(false) }
    var companyCodeWasFocused by remember { mutableStateOf(false) }
    var companyNameShowError by remember { mutableStateOf(false) }
    var companyCodeShowError by remember { mutableStateOf(false) }

    Text(
        text = "Configuración de Empresa",
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(16.dp))

    Column {
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = companyOption == "crear",
                onClick = { onCompanyOptionChange("crear") }
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
                selected = companyOption == "asociar",
                onClick = { onCompanyOptionChange("asociar") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Asociarse a empresa existente")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (companyOption == "crear") {
        OutlinedTextField(
            value = companyName,
            onValueChange = onCompanyNameChange,
            label = { Text("Nombre de la empresa *") },
            isError = companyNameShowError && companyName.isBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { f ->
                    if (f.isFocused) {
                        companyNameWasFocused = true
                    } else if (companyNameWasFocused) {
                        companyNameShowError = true
                    }
                }
        )
    } else {
        OutlinedTextField(
            value = companyCode,
            onValueChange = onCompanyCodeChange,
            label = { Text("Código de empresa *") },
            isError = companyCodeShowError && companyCode.isBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { f ->
                    if (f.isFocused) {
                        companyCodeWasFocused = true
                    } else if (companyCodeWasFocused) {
                        companyCodeShowError = true
                    }
                }
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

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
            Text(submitLabel)
        }
    }
}
