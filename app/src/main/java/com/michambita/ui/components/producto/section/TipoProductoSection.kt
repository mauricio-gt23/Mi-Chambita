package com.michambita.ui.components.producto.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.data.enums.EnumTipoProducto
import com.michambita.ui.components.producto.SectionCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun TipoProductoSection(
    tipoProducto: EnumTipoProducto,
    precio: String,
    unidadMedida: String,
    stock: String,
    onPrecioChange: (String) -> Unit,
    onUnidadMedidaChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onTipoProductoChange: (EnumTipoProducto) -> Unit,
) {
    val opciones = listOf(
        EnumTipoProducto.INVENTARIABLE to "Inventariable",
        EnumTipoProducto.NO_INVENTARIABLE to "No inventariable",
        EnumTipoProducto.SERVICIO to "Servicio"
    )
    val unidades = listOf("Unidad", "Kg", "g", "L", "mL", "Caja", "Paquete")

    SectionCard(title = "Tipo de producto") {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(opciones.size) { idx ->
                val (tp, label) = opciones[idx]
                FilterChip(
                    selected = tipoProducto == tp,
                    onClick = { onTipoProductoChange(tp) },
                    label = { Text(label) }
                )
            }
        }

        Text(
            "El tipo define si requiere stock o unidad de medida",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.padding(top = 12.dp))

        val precioLabel = when (tipoProducto) {
            EnumTipoProducto.INVENTARIABLE -> "Precio por unidad"
            EnumTipoProducto.NO_INVENTARIABLE -> if (unidadMedida.isNotBlank()) "Precio por $unidadMedida" else "Precio"
            EnumTipoProducto.SERVICIO -> "Precio del servicio"
        }

        OutlinedTextField(
            value = precio,
            onValueChange = onPrecioChange,
            label = { Text(precioLabel) },
            leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
            trailingIcon = {
                if (precio.isNotEmpty()) {
                    IconButton(onClick = { onPrecioChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar precio")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        when (tipoProducto) {
            EnumTipoProducto.INVENTARIABLE -> {
                Spacer(Modifier.height(10.dp))
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
            EnumTipoProducto.NO_INVENTARIABLE -> {
                Spacer(Modifier.height(10.dp))
                Text("Unidad de medida", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(3.dp))
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
}