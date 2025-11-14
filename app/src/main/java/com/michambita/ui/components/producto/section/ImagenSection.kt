package com.michambita.ui.components.producto.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michambita.ui.components.producto.SectionCard

@Composable
fun ImagenSection(
    imagenUrl: String?,
    imageLoader: ImageLoader,
    isSubiendoImagen: Boolean,
    isImagenCargadaExitosa: Boolean,
    onBorrarImagenClick: () -> Unit,
    onPreviewLoadingChange: (Boolean) -> Unit,
    onSeleccionarImagenClick: () -> Unit,
) {
    var showPreview by remember { mutableStateOf(false) }
    var isPreviewLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    SectionCard(title = "Imagen del producto") {
        if (!isImagenCargadaExitosa) {
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
        }

        if (isImagenCargadaExitosa && imagenUrl != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Imagen cargada exitosamente",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            isPreviewLoading = true
                            onPreviewLoadingChange(true)
                            val request = ImageRequest.Builder(context)
                                .data(imagenUrl)
                                .listener(
                                    onSuccess = { _, _ ->
                                        isPreviewLoading = false
                                        onPreviewLoadingChange(false)
                                        showPreview = true
                                    },
                                    onError = { _, _ ->
                                        isPreviewLoading = false
                                        onPreviewLoadingChange(false)
                                    }
                                )
                                .build()
                            imageLoader.enqueue(request)
                        },
                        enabled = !isPreviewLoading
                    ) { Text("Previsualizar") }
                    Button(onClick = onBorrarImagenClick) { Text("Borrar foto") }
                }
                if (isPreviewLoading) {
                    Spacer(Modifier.height(12.dp))
                }
            }

            if (showPreview) {
                AlertDialog(
                    onDismissRequest = { showPreview = false },
                    confirmButton = { TextButton(onClick = { showPreview = false }) { Text("Cerrar") } },
                    title = { Text("Previsualización") },
                    text = {
                        AsyncImage(
                            model = imagenUrl,
                            imageLoader = imageLoader,
                            contentDescription = "Previsualización",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.Center
                        )
                    }
                )
            }
        } else {
            Text("No hay imagen seleccionada", style = MaterialTheme.typography.bodySmall)
        }
    }
}