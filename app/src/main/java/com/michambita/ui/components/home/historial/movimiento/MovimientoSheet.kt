package com.michambita.ui.components.home.historial.movimiento

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
import com.michambita.data.enums.EnumModoOperacion

@Composable
fun MovimientoSheet(
    modifier: Modifier,
    modoOperacion: EnumModoOperacion,
    tipoOperacion: String,
    titulo: String,
    monto: String,
    ventaRapida: Boolean,
    onTituloChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    onVentaRapidaChange: (Boolean) -> Unit,
    onGuardarClick: () -> Unit
) {
    val heighSheet = if (!ventaRapida) modifier.fillMaxHeight() else Modifier.height(320.dp)

    Column(
        modifier = heighSheet
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Venta rapida", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.width(8.dp))
                Switch(checked = ventaRapida, onCheckedChange = onVentaRapidaChange)
            }
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