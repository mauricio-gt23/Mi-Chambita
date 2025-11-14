package com.michambita.ui.components.inventario.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Producto
import com.michambita.data.enum.EnumTipoProducto

@Composable
fun ItemCard(
    producto: Producto,
    onChangeStock: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$${producto.precio}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            when (producto.tipoProducto) {
                EnumTipoProducto.INVENTARIABLE -> {
                    Row {
                        AssistChip(
                            onClick = {},
                            label = { Text("Stock: ${(producto.stock ?: 0)}") },
                            leadingIcon = { Icon(Icons.Rounded.Inventory2, contentDescription = null) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                leadingIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        if (!producto.id.isNullOrBlank()) {
                            IconButton(onClick = {
                                val ns = (producto.stock ?: 0) - 1
                                onChangeStock(producto.id!!, ns.coerceAtLeast(0))
                            }) {
                                Icon(Icons.Rounded.Remove, contentDescription = "Disminuir stock")
                            }
                            IconButton(onClick = {
                                val ns = (producto.stock ?: 0) + 1
                                onChangeStock(producto.id!!, ns)
                            }) {
                                Icon(Icons.Rounded.Add, contentDescription = "Aumentar stock")
                            }
                        }
                    }
                }
                EnumTipoProducto.NO_INVENTARIABLE -> {
                    AssistChip(
                        onClick = {},
                        label = { Text("Unidad medida: ${producto.unidadMedida.ifBlank { "Sin unidad" }}") },
                        leadingIcon = { Icon(Icons.Filled.Straighten, contentDescription = null) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
                EnumTipoProducto.SERVICIO -> {}
            }
        }
    }
}