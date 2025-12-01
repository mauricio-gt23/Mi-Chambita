package com.michambita.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.michambita.domain.model.Movimiento
import com.michambita.navigation.Screen
import com.michambita.ui.components.home.historial.EncabezadoHistorial
import com.michambita.ui.components.home.historial.MovimientoHistorial
import com.michambita.ui.viewmodel.HomeUiState

@Composable
fun HomeContent(
    uiState: HomeUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
    movimientosPendientes: List<Movimiento>,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit,
    onSincronizarMovimiento: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ResumenDiario(ventas = uiState.ventas, gastos = uiState.gastos, isInitialLoading = uiState.isInitialLoading)

        HomeAcciones(
            onRegistrarVenta = onRegistrarVenta,
            onRegistrarGasto = onRegistrarGasto,
            onProductosClick = { navController.navigate(Screen.Producto.route) },
            onInventarioClick = { navController.navigate(Screen.Inventario.route) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        EncabezadoHistorial(modifier, movimientosPendientes, onSincronizarMovimiento)
        MovimientoHistorial(
            movimientosPendientes = movimientosPendientes,
            onEditarMovimiento = onEditarMovimiento,
            onEliminarMovimiento = onEliminarMovimiento,
            isInitialLoading = uiState.isInitialLoading
        )
    }
}
