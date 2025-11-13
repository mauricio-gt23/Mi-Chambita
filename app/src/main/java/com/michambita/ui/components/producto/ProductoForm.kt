package com.michambita.ui.components.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.michambita.data.enum.EnumTipoProducto

@Composable
fun ProductoForm(
    nombre: String,
    descripcion: String,
    precio: String,
    unidadMedida: String,
    tipoProducto: EnumTipoProducto,
    stock: String,
    imagenUrl: String?,
    isSubiendoImagen: Boolean,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onUnidadMedidaChange: (String) -> Unit,
    onTipoProductoChange: (EnumTipoProducto) -> Unit,
    onStockChange: (String) -> Unit,
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
            precio = precio,
            onNombreChange = onNombreChange,
            onDescripcionChange = onDescripcionChange,
            onPrecioChange = onPrecioChange
        )

        TipoProductoSection(
            tipoProducto = tipoProducto,
            onTipoProductoChange = onTipoProductoChange
        )

        ImagenSection(
            imagenUrl = imagenUrl,
            isSubiendoImagen = isSubiendoImagen,
            onSeleccionarImagenClick = onSeleccionarImagenClick
        )

        TangibleSection(
            tipoProducto = tipoProducto,
            unidadMedida = unidadMedida,
            onUnidadMedidaChange = onUnidadMedidaChange,
            stock = stock,
            onStockChange = onStockChange
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
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun DatosBasicosSection(
    nombre: String,
    descripcion: String,
    precio: String,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
) {
    SectionCard(title = "Datos básicos") {
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
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

@Composable
private fun TangibleSection(
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

@Composable
private fun TipoProductoSection(
    tipoProducto: EnumTipoProducto,
    onTipoProductoChange: (EnumTipoProducto) -> Unit,
) {
    SectionCard(title = "Tipo de producto") {
        val opciones = listOf(
            EnumTipoProducto.INVENTARIABLE to "Inventariable",
            EnumTipoProducto.NO_INVENTARIABLE to "No inventariable",
            EnumTipoProducto.SERVICIO to "Servicio"
        )
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
    }
}

@Composable
private fun ImagenSection(
    imagenUrl: String?,
    isSubiendoImagen: Boolean,
    onSeleccionarImagenClick: () -> Unit,
) {
    SectionCard(title = "Imagen del producto") {
        Button(
            onClick = onSeleccionarImagenClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubiendoImagen
        ) {
            if (isSubiendoImagen) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Subiendo imagen...")
            } else {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Seleccionar foto del producto")
            }
        }

        Spacer(Modifier.height(8.dp))

        if (imagenUrl != null) {
            Text("Imagen subida:")
            Spacer(Modifier.height(4.dp))
            Text(imagenUrl, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        } else {
            Text("No hay imagen seleccionada", style = MaterialTheme.typography.bodySmall)
        }
    }
}