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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.michambita.di.image.ImageLoaderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import com.michambita.ui.viewmodel.ProductoViewModel
import com.michambita.ui.common.UiState
import com.michambita.ui.components.widget.LoadingOverlay
import com.michambita.ui.components.producto.ProductoForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateSaveProducto.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val uiUploadState by viewModel.uiStateUploadImage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        var previewLoading by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val imageLoader = remember {
            val entryPoint = EntryPointAccessors.fromApplication(context, ImageLoaderEntryPoint::class.java)
            entryPoint.imageLoader()
        }
        val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.subirImagen(it) { /* resultado de subida */ }
            }
        }

        ProductoForm(
            nombre = formState.nombre,
            descripcion = formState.descripcion,
            precio = formState.precio,
            unidadMedida = formState.unidadMedida,
            tipoProducto = formState.tipoProducto,
            stock = formState.stock,
            imagenUrl = formState.imagenUrl,
            imageLoader = imageLoader,
            isSubiendoImagen = uiUploadState is UiState.Loading,
            isImagenCargadaExitosa = uiUploadState is UiState.Success,
            onBorrarImagenClick = { viewModel.borrarImagen() },
            onPreviewLoadingChange = { previewLoading = it },
            onNombreChange = viewModel::updateNombre,
            onDescripcionChange = viewModel::updateDescripcion,
            onPrecioChange = viewModel::updatePrecio,
            onUnidadMedidaChange = viewModel::updateUnidadMedida,
            onTipoProductoChange = viewModel::setTipoProducto,
            onStockChange = viewModel::updateStock,
            onSeleccionarImagenClick = {
                imagePicker.launch("image/*")
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

        if (previewLoading) {
            LoadingOverlay(modifier = Modifier, message = "Cargando previsualización...")
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
