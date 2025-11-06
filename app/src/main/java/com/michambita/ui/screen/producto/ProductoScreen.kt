package com.michambita.ui.screen.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.domain.model.Producto
import com.michambita.ui.viewmodel.ProductoViewModel
import com.michambita.ui.common.UiState
import com.michambita.ui.components.widget.LoadingOverlay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateSaveProducto.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("") }
    var esIntangible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        ProductoForm(
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            unidadMedida = unidadMedida,
            esIntangible = esIntangible,
            onNombreChange = { nombre = it },
            onDescripcionChange = { descripcion = it },
            onPrecioChange = { precio = it },
            onUnidadMedidaChange = { unidadMedida = it },
            onEsIntangibleChange = { esIntangible = it },
            onSeleccionarImagenClick = {
                // TODO: lógica seleccionar imagen
            },
            onGuardarClick = {
                if (nombre.isBlank()) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Ingrese el nombre del producto")
                    }
                    return@ProductoForm
                }
                val producto = Producto(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim().ifEmpty { null },
                    precio = precio.toDoubleOrNull() ?: 0.0,
                    unidadMedida = unidadMedida.trim().ifEmpty { "" },
                    esIntangible = esIntangible
                )
                viewModel.saveProducto(producto)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        )

        when (uiState) {
            is UiState.Loading -> {
                LoadingOverlay(modifier = Modifier, message = "Guardando Producto...")
            }
            is UiState.Success -> {
                LaunchedEffect(uiState) {
                    snackbarHostState.showSnackbar(
                        message = "Producto guardado correctamente",
                        duration = SnackbarDuration.Short
                    )
                    // Reset form
                    nombre = ""
                    descripcion = ""
                    precio = ""
                    unidadMedida = ""
                    esIntangible = false
                }
            }
            is UiState.Error -> {
                LaunchedEffect(uiState) {
                    val msg = (uiState as UiState.Error).message
                    snackbarHostState.showSnackbar(message = msg)
                }
            }
            else -> {}
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
        )
    }
}
