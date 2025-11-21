package com.michambita.ui.screen

import android.util.Log
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
    navController: NavController, viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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

    LaunchedEffect(uiState.bottomSheetVisible) {
        if (uiState.bottomSheetVisible) sheetState.expand()
        else sheetState.hide()
    }

    if (uiState.bottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { viewModel.hideBottomSheet() },
            shape = RoundedCornerShape(
                topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp
            ),
            dragHandle = { BottomSheetDefaults.DragHandle() }) {
            MovimientoSheet(
                modifier = Modifier,
                modoOperacion = uiState.modoOperacion,
                tipoOperacion = uiState.tipoMovimiento,
                titulo = uiState.movimientoRegEdit?.descripcion ?: "",
                monto = uiState.movimientoRegEdit?.monto?.toPlainString() ?: "",
                ventaRapida = uiState.ventaRapida,
                onTituloChange = { viewModel.onMovimientoChange("titulo", it) },
                onMontoChange = { viewModel.onMovimientoChange("monto", it) },
                onVentaRapidaChange = viewModel::setVentaRapida,
                onGuardarClick = viewModel::onGuardarMovimiento
            )
        }
    }
}