package com.michambita.feature.producto.screen

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
import com.michambita.core.ui.di.ImageLoaderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import com.michambita.feature.producto.viewmodel.ProductoViewModel
import com.michambita.feature.producto.viewmodel.ImagenViewModel
import com.michambita.core.common.UiState
import com.michambita.core.ui.components.widget.LoadingOverlay
import com.michambita.feature.producto.components.ProductoForm
import com.michambita.core.domain.enums.EnumModoOperacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    modifier: Modifier = Modifier,
    productId: String? = null,
    onSaveSuccess: () -> Unit = {},
    productoViewModel: ProductoViewModel = hiltViewModel(),
    imagenViewModel: ImagenViewModel = hiltViewModel()
) {
    val uiState by productoViewModel.uiStateSaveProducto.collectAsStateWithLifecycle()
    val uiFormState by productoViewModel.uiFormState.collectAsStateWithLifecycle()
    val modo by productoViewModel.modoOperacion.collectAsStateWithLifecycle()

    val imageUrl by imagenViewModel.imageUrl.collectAsStateWithLifecycle()
    val uiImageState by imagenViewModel.uiStateUploadImage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(productId) {
        if (!productId.isNullOrBlank()) {
            productoViewModel.cargarProducto(productId)
        }
    }

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
            uri?.let { imagenViewModel.subirImagen(it) }
        }

        val titulo = if (modo == EnumModoOperacion.EDITAR) "Editar Producto" else "Registrar Producto"
        ProductoForm(
            titulo = titulo,
            nombre = uiFormState.nombre,
            descripcion = uiFormState.descripcion,
            precio = uiFormState.precio,
            unidadMedida = uiFormState.unidadMedida,
            tipoProducto = uiFormState.tipoProducto,
            stock = uiFormState.stock,
            imagenUrl = imageUrl,
            imageLoader = imageLoader,
            isSubiendoImagen = uiImageState is UiState.Loading,
            isImagenCargadaExitosa = uiImageState is UiState.Success,
            onBorrarImagenClick = { imagenViewModel.borrarImagen() },
            onPreviewLoadingChange = { previewLoading = it },
            onNombreChange = productoViewModel::updateNombre,
            onDescripcionChange = productoViewModel::updateDescripcion,
            onPrecioChange = productoViewModel::updatePrecio,
            onUnidadMedidaChange = productoViewModel::updateUnidadMedida,
            onTipoProductoChange = productoViewModel::setTipoProducto,
            onStockChange = productoViewModel::updateStock,
            onSeleccionarImagenClick = {
                imagePicker.launch("image/*")
            },
            onGuardarClick = {
                productoViewModel.guardarProducto(imageUrl)
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
                    if (!productId.isNullOrBlank()) {
                        onSaveSuccess()
                    }
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
