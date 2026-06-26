package com.michambita.feature.home.screen

import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michambita.domain.enums.BusinessType
import com.michambita.feature.home.config.HomeUiConfig
import com.michambita.feature.home.components.HomeContent
import com.michambita.feature.home.components.historial.movimiento.MovimientoSheet
import com.michambita.feature.home.viewmodel.HomeViewModel
import com.michambita.feature.inventario.intentmodel.InventarioIntent
import com.michambita.feature.inventario.intentmodel.InventarioIntentModel
import com.michambita.common.UiState
import com.michambita.domain.model.Item
import com.michambita.feature.item.viewmodel.MovimientoViewModel
import com.michambita.ui.components.widget.AlertModal
import com.michambita.ui.components.widget.ErrorDisplay
import com.michambita.ui.components.widget.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    businessType: BusinessType,
    homeViewModel: HomeViewModel = hiltViewModel(),
    inventarioIntentModel: InventarioIntentModel = hiltViewModel(),
    movimientoViewModel: MovimientoViewModel = hiltViewModel()
) {
    val uiConfig = remember(businessType) { HomeUiConfig.from(businessType) }

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
    val movimientos by homeViewModel.movimientos.collectAsStateWithLifecycle()

    val movimientoUiState by movimientoViewModel.uiState.collectAsStateWithLifecycle()

    // Only load items if the motor needs them
    val items: List<Item> = if (uiConfig.showItemtPicker) {
        val inventarioState by inventarioIntentModel.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(Unit) { inventarioIntentModel.sendIntent(InventarioIntent.LoadItems) }
        inventarioState.items
    } else {
        emptyList()
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    HomeContent(
        uiState = homeUiState,
        uiConfig = uiConfig,
        navController = navController,
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
        onSincronizarMovimiento = homeViewModel::onSincronizarMovimientos
    )

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
                        homeViewModel.hideBottomSheet()
                    }
                )
        }
    }

    when (val state = uiState) {
        is UiState.Empty -> {}
        is UiState.Loading -> {
            LoadingOverlay(modifier = Modifier, message = "Sincronizando...")
        }
        is UiState.Success -> {
            AlertModal(
                modifier = Modifier,
                title = state.data,
                message = "",
                showDismissButton = false,
                onConfirm = { homeViewModel.clearUiState() },
                onDismissRequest = { homeViewModel.clearUiState() }
            )
        }
        is UiState.Error -> {
            ErrorDisplay(
                modifier = Modifier,
                errorMessage = state.message,
                onDismiss = { homeViewModel.clearUiState() }
            )
        }
    }
}
