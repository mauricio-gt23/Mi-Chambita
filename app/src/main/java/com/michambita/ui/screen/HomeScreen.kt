package com.michambita.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michambita.domain.model.Movimiento
import com.michambita.navigation.Screen
import com.michambita.ui.components.*
import com.michambita.ui.viewmodel.HomeUiState
import com.michambita.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Obtener estado del ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    // Configuración del BottomSheet
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
    )

    // Controlar la visibilidad del BottomSheet basado en el estado del ViewModel
    LaunchedEffect(uiState.bottomSheetVisible) {
        if (uiState.bottomSheetVisible) {
            scaffoldState.bottomSheetState.expand()
        } else {
            scaffoldState.bottomSheetState.hide()
        }
    }

    // Cuando el BottomSheet se cierra manualmente, actualizar el estado en el ViewModel
    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        if (!scaffoldState.bottomSheetState.isVisible && uiState.bottomSheetVisible) {
            viewModel.hideBottomSheet()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
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
    ) { padding ->
        HomeContent(
            uiState = uiState,
            navController = navController,
            modifier = Modifier.padding(padding),
            movimientosPendientes = movimientos,
            onRegistrarVenta = viewModel::onRegistrarVenta,
            onRegistrarGasto = viewModel::onRegistrarGasto,
            onEditarMovimiento = viewModel::onEditarMovimiento,
            onEliminarMovimiento = viewModel::deleteMovimiento
        )
    }
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
    movimientosPendientes: List<Movimiento>,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ResumenDiario(ventas = uiState.ventas, gastos = uiState.gastos)

        HomeAcciones(
            onRegistrarVenta = onRegistrarVenta,
            onRegistrarGasto = onRegistrarGasto,
            onProductosClick = { navController.navigate(Screen.Producto.route) },
            onInventarioClick = { navController.navigate(Screen.Inventario.route) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        EncabezadoHistorial()

        MovimientoHistorial(
            movimientosPendientes = movimientosPendientes,
            onEditarMovimiento = onEditarMovimiento,
            onEliminarMovimiento = onEliminarMovimiento
        )
    }
}