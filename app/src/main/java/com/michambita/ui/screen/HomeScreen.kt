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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    inventarioViewModel: InventarioViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val movimientos by homeViewModel.movimientos.collectAsStateWithLifecycle()

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
        uiState = uiState,
        navController = navController,
        modifier = Modifier,
        movimientosPendientes = movimientos,
        onRegistrarVenta = homeViewModel::onRegistrarVenta,
        onRegistrarGasto = homeViewModel::onRegistrarGasto,
        onEditarMovimiento = homeViewModel::onEditarMovimiento,
        onEliminarMovimiento = homeViewModel::deleteMovimiento
    )

    LaunchedEffect(uiState.bottomSheetVisible) {
        if (uiState.bottomSheetVisible) sheetState.expand()
        else sheetState.hide()
    }

    if (uiState.bottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { homeViewModel.hideBottomSheet() },
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
                productos = productos,
                onTituloChange = { homeViewModel.onMovimientoChange("titulo", it) },
                onMontoChange = { homeViewModel.onMovimientoChange("monto", it) },
                onVentaRapidaChange = homeViewModel::setVentaRapida,
                onGuardarClick = homeViewModel::onGuardarMovimiento,
                onItemsChange = { itemsDomain ->
                    homeViewModel.setItemsMovimiento(itemsDomain)
                }
            )
        }
    }
}