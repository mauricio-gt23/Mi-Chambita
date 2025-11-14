package com.michambita.ui.components.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.michambita.data.enum.EnumTipoProducto
import coil.ImageLoader
import coil.compose.AsyncImage
import com.michambita.ui.components.producto.section.DatosBasicosSection
import com.michambita.ui.components.producto.section.ImagenSection
import com.michambita.ui.components.producto.section.TangibleSection
import com.michambita.ui.components.producto.section.TipoProductoSection

@Composable
fun ProductoForm(
    nombre: String,
    descripcion: String,
    precio: String,
    unidadMedida: String,
    tipoProducto: EnumTipoProducto,
    stock: String,
    imagenUrl: String?,
    imageLoader: ImageLoader,
    isSubiendoImagen: Boolean,
    isImagenCargadaExitosa: Boolean,
    onBorrarImagenClick: () -> Unit,
    onPreviewLoadingChange: (Boolean) -> Unit,
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

        TangibleSection(
            tipoProducto = tipoProducto,
            unidadMedida = unidadMedida,
            onUnidadMedidaChange = onUnidadMedidaChange,
            stock = stock,
            onStockChange = onStockChange
        )

        ImagenSection(
            imagenUrl = imagenUrl,
            imageLoader = imageLoader,
            isSubiendoImagen = isSubiendoImagen,
            isImagenCargadaExitosa = isImagenCargadaExitosa,
            onBorrarImagenClick = onBorrarImagenClick,
            onPreviewLoadingChange = onPreviewLoadingChange,
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