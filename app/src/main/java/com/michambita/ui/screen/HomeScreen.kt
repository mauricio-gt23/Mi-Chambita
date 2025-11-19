package com.michambita.ui.screen

import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michambita.ui.components.home.HomeContent
import com.michambita.ui.components.home.historial.movimiento.MovimientoSheet
import com.michambita.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    LaunchedEffect(uiState.bottomSheetVisible) {
        if (uiState.bottomSheetVisible) {
            sheetState.partialExpand()
        }
    }

    HomeContent(
        uiState = uiState,
        navController = navController,
        modifier = Modifier,
        movimientosPendientes = movimientos,
        onRegistrarVenta = viewModel::onRegistrarVenta,
        onRegistrarGasto = viewModel::onRegistrarGasto,
        onEditarMovimiento = viewModel::onEditarMovimiento,
        onEliminarMovimiento = viewModel::deleteMovimiento
    )

    if (uiState.bottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideBottomSheet() },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            MovimientoSheet(
                modoOperacion = uiState.modoOperacion,
                tipoOperacion = uiState.tipoMovimiento,
                titulo = uiState.movimientoRegEdit?.descripcion ?: "",
                monto = uiState.movimientoRegEdit?.monto?.toPlainString() ?: "",
                onTituloChange = { viewModel.onMovimientoChange("titulo", it) },
                onMontoChange = { viewModel.onMovimientoChange("monto", it) },
                onGuardarClick = viewModel::onGuardarMovimiento
            )
        }
    }
}