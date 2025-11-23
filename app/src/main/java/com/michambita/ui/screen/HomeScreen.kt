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
import com.michambita.ui.viewmodel.InventarioViewModel
import com.michambita.ui.common.UiState
import com.michambita.domain.model.Producto
import com.michambita.ui.viewmodel.MovimientoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    inventarioViewModel: InventarioViewModel = hiltViewModel(),
    movimientoViewModel: MovimientoViewModel = hiltViewModel()
) {
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val movimientos by homeViewModel.movimientos.collectAsStateWithLifecycle()

    val movimientoUiState by movimientoViewModel.uiState.collectAsStateWithLifecycle()

    val uiStateProductos by inventarioViewModel.uiStateGetAllProducto.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { inventarioViewModel.getAllProducto() }
    val productos: List<Producto> = when (val s = uiStateProductos) {
        is UiState.Success -> s.data
        else -> emptyList()
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    HomeContent(
        uiState = homeUiState,
        navController = navController,
        modifier = Modifier,
        movimientosPendientes = movimientos,
        onRegistrarVenta = movimientoViewModel::onRegistrarVenta,
        onRegistrarGasto = movimientoViewModel::onRegistrarGasto,
        onEditarMovimiento = movimientoViewModel::onEditarMovimiento,
        onEliminarMovimiento = movimientoViewModel::deleteMovimiento
    )

    LaunchedEffect(movimientoUiState.bottomSheetVisible) {
        if (movimientoUiState.bottomSheetVisible) sheetState.expand()
        else sheetState.hide()
    }

    if (movimientoUiState.bottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { movimientoViewModel.hideBottomSheet() },
            shape = RoundedCornerShape(
                topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp
            ),
            dragHandle = { BottomSheetDefaults.DragHandle() }) {
            MovimientoSheet(
                modifier = Modifier,
                modoOperacion = movimientoUiState.modoOperacion,
                tipoOperacion = movimientoUiState.tipoMovimiento,
                titulo = movimientoUiState.movimientoRegEdit?.descripcion ?: "",
                monto = movimientoUiState.movimientoRegEdit?.monto?.toPlainString() ?: "",
                ventaRapida = movimientoUiState.ventaRapida,
                productos = productos,
                onTituloChange = { movimientoViewModel.onMovimientoChange("titulo", it) },
                onMontoChange = { movimientoViewModel.onMovimientoChange("monto", it) },
                onVentaRapidaChange = movimientoViewModel::setVentaRapida,
                onGuardarClick = movimientoViewModel::onGuardarMovimiento,
                onItemsChange = { items ->
                    movimientoViewModel.setItemsMovimiento(items)
                }
            )
        }
    }
}