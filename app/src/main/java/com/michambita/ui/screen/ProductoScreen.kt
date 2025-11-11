package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.domain.model.Producto
import com.michambita.ui.viewmodel.ProductoViewModel
import com.michambita.ui.common.UiState
import com.michambita.ui.components.widget.LoadingOverlay
import com.michambita.ui.components.producto.ProductoForm
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateSaveProducto.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        ProductoForm(
            nombre = formState.nombre,
            descripcion = formState.descripcion,
            precio = formState.precio,
            unidadMedida = formState.unidadMedida,
            tipoProducto = formState.tipoProducto,
            stock = formState.stock,
            onNombreChange = viewModel::updateNombre,
            onDescripcionChange = viewModel::updateDescripcion,
            onPrecioChange = viewModel::updatePrecio,
            onUnidadMedidaChange = viewModel::updateUnidadMedida,
            onTipoProductoChange = viewModel::setTipoProducto,
            onStockChange = viewModel::updateStock,
            onSeleccionarImagenClick = {
                // TODO: lógica seleccionar imagen
            },
            onGuardarClick = {
                viewModel.guardarProducto()
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
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
