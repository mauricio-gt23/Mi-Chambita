package com.michambita.feature.item.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.michambita.feature.item.config.ItemFormUiConfig
import com.michambita.feature.item.components.section.DatosBasicosSection
import com.michambita.feature.item.components.section.ImagenSection

@Composable
fun ItemForm(
    titulo: String,
    nombre: String,
    descripcion: String,
    precio: String,
    unidadMedida: String,
    stock: String,
    imagenUrl: String?,
    imageLoader: ImageLoader,
    isSubiendoImagen: Boolean,
    isImagenCargadaExitosa: Boolean,
    uiConfig: ItemFormUiConfig,
    onBorrarImagenClick: () -> Unit,
    onPreviewLoadingChange: (Boolean) -> Unit,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onUnidadMedidaChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onSeleccionarImagenClick: () -> Unit,
    onGuardarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)

        DatosBasicosSection(
            nombre = nombre,
            descripcion = descripcion,
            onNombreChange = onNombreChange,
            onDescripcionChange = onDescripcionChange
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Precio y detalles", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = precio,
                    onValueChange = onPrecioChange,
                    label = { Text("Precio") },
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
                        imeAction = if (uiConfig.showStock || uiConfig.showUnidadMedida) ImeAction.Next else ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (uiConfig.showStock) {
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
                            imeAction = if (uiConfig.showUnidadMedida) ImeAction.Next else ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (uiConfig.showUnidadMedida) {
                    val unidades = listOf("Unidad", "Kg", "g", "L", "mL", "Caja", "Paquete")
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
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (uiConfig.showImageSection) {
            ImagenSection(
                imagenUrl = imagenUrl,
                imageLoader = imageLoader,
                isSubiendoImagen = isSubiendoImagen,
                isImagenCargadaExitosa = isImagenCargadaExitosa,
                onBorrarImagenClick = onBorrarImagenClick,
                onPreviewLoadingChange = onPreviewLoadingChange,
                onSeleccionarImagenClick = onSeleccionarImagenClick
            )
        }

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
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
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
