package com.michambita.feature.item.components.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.michambita.feature.item.components.SectionCard
import com.michambita.ui.components.widget.RequiredTextField

@Composable
fun DatosBasicosSection(
    nombre: String,
    descripcion: String,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
) {
    SectionCard(title = "Datos básicos") {
        RequiredTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = "Nombre",
            leadingIcon = { Icon(Icons.Default.Inventory, contentDescription = null) },
            trailingIcon = {
                if (nombre.isNotEmpty()) {
                    IconButton(onClick = { onNombreChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar nombre")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        RequiredTextField(
            value = descripcion,
            onValueChange = onDescripcionChange,
            label = "Descripción",
            isRequired = false,
            leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
            defaultSupportingText = { Text("Opcional. Añade detalles o notas") },
            trailingIcon = {
                if (descripcion.isNotEmpty()) {
                    IconButton(onClick = { onDescripcionChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar descripción")
                    }
                }
            },
            maxLines = 4,
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
    }
}