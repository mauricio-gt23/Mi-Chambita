package com.michambita.feature.home.components

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
import com.michambita.core.domain.model.Movimiento
import com.michambita.core.common.Screen
import com.michambita.feature.home.components.historial.EncabezadoHistorial
import com.michambita.feature.home.components.historial.MovimientoHistorial
import com.michambita.feature.home.viewmodel.HomeUiState
import com.michambita.core.common.DateUtils

@Composable
fun HomeContent(
    uiState: HomeUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
    movimientos: List<Movimiento>,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit,
    onSincronizarMovimiento: () -> Unit
) {
    val movimientosHoy = movimientos
        .filter { DateUtils.isToday(it.fechaRegistro) }
        .sortedByDescending { it.fechaRegistro.time }

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

        Spacer(modifier = Modifier.height(10.dp))

        EncabezadoHistorial(modifier, movimientosHoy, onSincronizarMovimiento)
        MovimientoHistorial(
            movimientos = movimientosHoy,
            onEditarMovimiento = onEditarMovimiento,
            onEliminarMovimiento = onEliminarMovimiento,
            isInitialLoading = uiState.isInitialLoading
        )
    }
}
