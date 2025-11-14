package com.michambita.ui.components.producto.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.michambita.data.enum.EnumTipoProducto
import com.michambita.ui.components.producto.SectionCard

@Composable
fun TangibleSection(
    tipoProducto: EnumTipoProducto,
    unidadMedida: String,
    onUnidadMedidaChange: (String) -> Unit,
    stock: String,
    onStockChange: (String) -> Unit,
) {
    when (tipoProducto) {
        EnumTipoProducto.INVENTARIABLE -> SectionCard(title = "Inventariable") {
            OutlinedTextField(
                value = stock,
                onValueChange = { input ->
                    val sanitized = input.filter { it.isDigit() }
                    onStockChange(sanitized)
                },
                label = { Text("Stock inicial") },
                leadingIcon = { Icon(Icons.Rounded.Inventory2, contentDescription = null) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        EnumTipoProducto.NO_INVENTARIABLE -> SectionCard(title = "No inventariable") {
            val unidades = listOf("Unidad", "Kg", "g", "L", "mL", "Caja", "Paquete")
            Text("Unidad de medida", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(unidades.size) { idx ->
                    val u = unidades[idx]
                    AssistChip(
                        onClick = { onUnidadMedidaChange(u) },
                        label = { Text(u) },
                        leadingIcon = { Icon(Icons.Default.Straighten, contentDescription = null) }
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = unidadMedida,
                onValueChange = onUnidadMedidaChange,
                placeholder = { Text("Ej: Kg, Unidad") },
                leadingIcon = { Icon(Icons.Default.Straighten, contentDescription = null) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
        }
        EnumTipoProducto.SERVICIO -> {}
    }
}