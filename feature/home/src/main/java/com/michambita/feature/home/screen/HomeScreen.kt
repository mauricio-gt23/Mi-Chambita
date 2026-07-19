package com.michambita.feature.home.screen

import androidx.compose.material3.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.domain.enums.BusinessType
import com.michambita.feature.home.config.HomeUiConfig
import com.michambita.feature.home.components.HomeContent
import com.michambita.feature.home.components.historial.movimiento.MovimientoSheet
import com.michambita.feature.home.viewmodel.HomeViewModel
import com.michambita.feature.home.viewmodel.MovimientoViewModel
import com.michambita.common.UiState
import com.michambita.ui.components.widget.AlertModal
import com.michambita.ui.components.widget.LoadingOverlay
import com.michambita.feature.home.components.StockShortageModal
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    businessType: BusinessType,
    onProductosClick: () -> Unit,
    onInventarioClick: () -> Unit,
    onHistorialClick: () -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel(),
    movimientoViewModel: MovimientoViewModel = hiltViewModel()
) {
    val uiConfig = remember(businessType) { HomeUiConfig.from(businessType) }

    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
    val movimientos by homeViewModel.movimientos.collectAsStateWithLifecycle()

    val movimientoUiState by movimientoViewModel.uiState.collectAsStateWithLifecycle()
    val operationState by movimientoViewModel.operationState.collectAsStateWithLifecycle()

    // Load items if the configuration needs them (e.g. to associate items to transactions)
    LaunchedEffect(uiConfig.loadItemList) {
        if (uiConfig.loadItemList) movimientoViewModel.loadItems()
    }
    val items = movimientoUiState.items

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Cerrar bottom sheet automáticamente cuando la operación es exitosa
    LaunchedEffect(operationState) {
        if (operationState is UiState.Success && homeUiState.bottomSheetVisible) {
            homeViewModel.hideBottomSheet()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HomeContent(
            uiState = homeUiState,
            uiConfig = uiConfig,
            onProductosClick = onProductosClick,
            onInventarioClick = onInventarioClick,
            modifier = Modifier,
            movimientos = movimientos,
            onRegistrarVenta = {
                movimientoViewModel.onRegistrarVenta()
                homeViewModel.showBottomSheet()
            },
            onRegistrarGasto = {
                movimientoViewModel.onRegistrarGasto()
                homeViewModel.showBottomSheet()
            },
            onEditarMovimiento = {
                movimientoViewModel.onEditarMovimiento(it)
                homeViewModel.showBottomSheet()
            },
            onEliminarMovimiento = movimientoViewModel::deleteMovimiento,
            onHistorialClick = onHistorialClick
            // onSincronizarMovimiento = homeViewModel::onSincronizarMovimientos // Offline-first: comentado para MVP online-first
        )
    }

    LaunchedEffect(homeUiState.bottomSheetVisible) {
        if (homeUiState.bottomSheetVisible) sheetState.expand()
        else sheetState.hide()
    }

    if (homeUiState.bottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { homeViewModel.hideBottomSheet() },
            shape = RoundedCornerShape(
                topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp
            ),
            dragHandle = { BottomSheetDefaults.DragHandle() }) {
                MovimientoSheet(
                    modifier = Modifier,
                    modoOperacion = movimientoUiState.modoOperacion,
                    movimiento = movimientoUiState.movimientoRegEdit,
                    items = items,
                    onMovimientoChange = movimientoViewModel::onMovimientoChange,
                    onGuardarClick = {
                        movimientoViewModel.onGuardarMovimiento()
                        // El sheet se cierra automáticamente vía LaunchedEffect cuando operationState = Success
                    }
                )
        }
    }

    // Estado de operación CRUD online
    when (val state = operationState) {
        is UiState.Loading -> {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                LoadingOverlay(modifier = Modifier, message = "Guardando...")
            }
        }
        is UiState.Success -> {
            AlertModal(
                title = state.data,
                message = "",
                confirmButtonText = "OK",
                showDismissButton = false,
                onConfirm = { movimientoViewModel.clearOperationState() },
                onDismissRequest = { movimientoViewModel.clearOperationState() }
            )
        }
        is UiState.Error -> {
            AlertModal(
                title = state.message,
                message = "",
                confirmButtonText = "OK",
                showDismissButton = false,
                onConfirm = { movimientoViewModel.clearOperationState() },
                onDismissRequest = { movimientoViewModel.clearOperationState() }
            )
        }
        else -> {}
    }

    if (movimientoUiState.stockShortages.isNotEmpty()) {
        StockShortageModal(
            shortages = movimientoUiState.stockShortages,
            onDismiss = { movimientoViewModel.clearStockShortages() }
        )
    }


    // ── Offline-first sync UI (commented out for MVP online-first) ──────
    // when (val state = uiState) {
    //     is UiState.Empty -> {}
    //     is UiState.Loading -> {
    //         LoadingOverlay(modifier = Modifier, message = "Sincronizando...")
    //     }
    //     is UiState.Success -> {
    //         AlertModal(
    //             modifier = Modifier,
    //             title = state.data,
    //             message = "",
    //             showDismissButton = false,
    //             onConfirm = { homeViewModel.clearUiState() },
    //             onDismissRequest = { homeViewModel.clearUiState() }
    //         )
    //     }
    //     is UiState.Error -> {
    //         ErrorDisplay(
    //             modifier = Modifier,
    //             errorMessage = state.message,
    //             onDismiss = { homeViewModel.clearUiState() }
    //         )
    //     }
    // }
}
