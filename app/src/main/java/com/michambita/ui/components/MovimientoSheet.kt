package com.michambita.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.michambita.data.enum.EnumModoOperacion

@Composable
fun MovimientoSheet(
    modoOperacion: EnumModoOperacion,
    tipoOperacion: String,
    titulo: String,
    monto: String,
    onTituloChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    onGuardarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding()
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (tipoOperacion == "V") Icons.Default.PointOfSale else Icons.Default.MoneyOff,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (modoOperacion == EnumModoOperacion.REGISTRAR) {
                    if (tipoOperacion == "V") "Registrar Venta" else "Registrar Gasto"
                } else {
                    "Editar Movimiento"
                },
                style = MaterialTheme.typography.titleLarge
            )
        }

        OutlinedTextField(
            value = titulo,
            onValueChange = onTituloChange,
            label = { Text("Descripción") },
            placeholder = {
                Text(if (tipoOperacion == "V") "Ej: venta de jugos" else "Ej: compra de vasos")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = monto,
            onValueChange = onMontoChange,
            label = { Text("Monto") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onGuardarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                when (modoOperacion) {
                    EnumModoOperacion.REGISTRAR ->
                        if (tipoOperacion == "V") "Guardar Venta" else "Guardar Gasto"

                    EnumModoOperacion.EDITAR -> "Guardar Cambios"
                }
            )
        }
    }
}