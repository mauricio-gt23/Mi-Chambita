package com.michambita.feature.item.screen

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
import com.michambita.ui.di.ImageLoaderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import com.michambita.domain.enums.BusinessType
import com.michambita.domain.enums.EnumModoOperacion
import com.michambita.feature.item.config.ItemFormUiConfig
import com.michambita.feature.item.viewmodel.ItemViewModel
import com.michambita.feature.item.viewmodel.ItemImageViewModel
import com.michambita.feature.item.components.ItemForm
import com.michambita.common.UiState
import com.michambita.ui.components.widget.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    businessType: BusinessType,
    modifier: Modifier = Modifier,
    itemId: String? = null,
    onSaveSuccess: () -> Unit = {},
    itemViewModel: ItemViewModel = hiltViewModel(),
    itemImageViewModel: ItemImageViewModel = hiltViewModel()
) {
    val uiState by itemViewModel.uiStateSaveItem.collectAsStateWithLifecycle()
    val uiLoadState by itemViewModel.uiStateLoadItem.collectAsStateWithLifecycle()
    val uiFormState by itemViewModel.uiFormState.collectAsStateWithLifecycle()
    val modo by itemViewModel.modoOperacion.collectAsStateWithLifecycle()

    val imageUrl by itemImageViewModel.imageUrl.collectAsStateWithLifecycle()
    val uiImageState by itemImageViewModel.uiStateUploadImage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val uiConfig = remember(businessType) { ItemFormUiConfig.from(businessType) }

    LaunchedEffect(itemId) {
        if (!itemId.isNullOrBlank()) {
            itemViewModel.cargarItem(itemId)
        }
    }

    LaunchedEffect(uiConfig) {
        if (modo == EnumModoOperacion.REGISTRAR) {
            itemViewModel.setItemType(uiConfig.itemType)
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
            uri?.let { itemImageViewModel.subirImagen(it) }
        }

        ItemForm(
            nombre = uiFormState.nombre,
            descripcion = uiFormState.descripcion,
            precio = uiFormState.precio,
            unidadMedida = uiFormState.unidadMedida,
            stock = uiFormState.stock,
            imagenUrl = imageUrl,
            imageLoader = imageLoader,
            isSubiendoImagen = uiImageState is UiState.Loading,
            isImagenCargadaExitosa = uiImageState is UiState.Success,
            uiConfig = uiConfig,
            onBorrarImagenClick = { itemImageViewModel.borrarImagen() },
            onPreviewLoadingChange = { previewLoading = it },
            onNombreChange = itemViewModel::updateNombre,
            onDescripcionChange = itemViewModel::updateDescripcion,
            onPrecioChange = itemViewModel::updatePrecio,
            onUnidadMedidaChange = itemViewModel::updateUnidadMedida,
            onStockChange = itemViewModel::updateStock,
            onSeleccionarImagenClick = {
                imagePicker.launch("image/*")
            },
            onGuardarClick = {
                itemViewModel.guardarItem(imageUrl)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        )

        when (uiState) {
            is UiState.Loading -> {
                LoadingOverlay(modifier = Modifier, message = "Guardando ${uiConfig.itemTypeLabel}...")
            }

            is UiState.Success -> {
                LaunchedEffect(uiState) {
                    snackbarHostState.showSnackbar(
                        message = "${uiConfig.itemTypeLabel} guardado correctamente",
                        duration = SnackbarDuration.Short
                    )
                    if (!itemId.isNullOrBlank()) {
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

        when (uiLoadState) {
            is UiState.Loading -> {
                LoadingOverlay(modifier = Modifier, message = "Cargando ${uiConfig.itemTypeLabel}...")
            }

            is UiState.Error -> {
                LaunchedEffect(uiLoadState) {
                    val msg = (uiLoadState as UiState.Error).message
                    snackbarHostState.showSnackbar(message = msg)
                    itemViewModel.clearLoadState()
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
