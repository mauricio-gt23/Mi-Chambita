package com.michambita.ui.components.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ProductoForm(
    nombre: String,
    descripcion: String,
    precio: String,
    unidadMedida: String,
    esIntangible: Boolean,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onUnidadMedidaChange: (String) -> Unit,
    onEsIntangibleChange: (Boolean) -> Unit,
    onSeleccionarImagenClick: () -> Unit,
    onGuardarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Registro de Producto", style = MaterialTheme.typography.headlineMedium)
        
        DatosBasicosSection(
            nombre = nombre,
            descripcion = descripcion,
            onNombreChange = onNombreChange,
            onDescripcionChange = onDescripcionChange
        )

        PrecioUnidadSection(
            precio = precio,
            unidadMedida = unidadMedida,
            onPrecioChange = onPrecioChange,
            onUnidadMedidaChange = onUnidadMedidaChange
        )

        TipoImagenSection(
            esIntangible = esIntangible,
            onEsIntangibleChange = onEsIntangibleChange,
            onSeleccionarImagenClick = onSeleccionarImagenClick
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onGuardarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Guardar")
        }
    }
}

@Composable
private fun DatosBasicosSection(
    nombre: String,
    descripcion: String,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Datos básicos", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre del producto") },
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

            OutlinedTextField(
                value = descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Descripción") },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                supportingText = { Text("Opcional. Añade detalles o notas") },
                trailingIcon = {
                    if (descripcion.isNotEmpty()) {
                        IconButton(onClick = { onDescripcionChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar descripción")
                        }
                    }
                },
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PrecioUnidadSection(
    precio: String,
    unidadMedida: String,
    onPrecioChange: (String) -> Unit,
    onUnidadMedidaChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Precio y unidad", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = onPrecioChange,
                label = { Text("Precio por unidad") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                trailingIcon = {
                    if (precio.isNotEmpty()) {
                        IconButton(onClick = { onPrecioChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar precio")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
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
        }
    }
}

@Composable
private fun TipoImagenSection(
    esIntangible: Boolean,
    onEsIntangibleChange: (Boolean) -> Unit,
    onSeleccionarImagenClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tipo de producto", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = esIntangible, onCheckedChange = onEsIntangibleChange)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Producto intangible")
                    Text(
                        "Activa si es un servicio o no tiene stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Divider(Modifier.padding(vertical = 12.dp))

            Text("Imagen del producto", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onSeleccionarImagenClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Seleccionar foto del producto")
            }
        }
    }
}