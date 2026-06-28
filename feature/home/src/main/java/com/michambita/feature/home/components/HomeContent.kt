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
import com.michambita.domain.model.Movimiento
import com.michambita.feature.home.config.HomeUiConfig
import com.michambita.common.Screen
import com.michambita.feature.home.components.historial.EncabezadoHistorial
import com.michambita.feature.home.components.historial.MovimientoHistorial
import com.michambita.feature.home.viewmodel.HomeUiState

@Composable
fun HomeContent(
    uiState: HomeUiState,
    uiConfig: HomeUiConfig,
    navController: NavController,
    modifier: Modifier = Modifier,
    movimientos: List<Movimiento>,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit
    // onSincronizarMovimiento: () -> Unit // Offline-first: comentado para MVP online-first
) {
    // Online-first: los movimientos ya vienen filtrados por hoy desde Firestore,
    // ordenados por fechaRegistro desc. Mostramos solo los últimos 10 en la UI.
    val movimientosParaMostrar = movimientos.take(10)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ResumenDiario(
            ventas = uiState.ventas,
            gastos = uiState.gastos,
            isInitialLoading = uiState.isInitialLoading
        )

        HomeAcciones(
            uiConfig = uiConfig,
            onRegistrarVenta = onRegistrarVenta,
            onRegistrarGasto = onRegistrarGasto,
            onProductosClick = { navController.navigate(Screen.Item.route) },
            onInventarioClick = { navController.navigate(Screen.Inventario.route) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        EncabezadoHistorial(modifier)
        MovimientoHistorial(
            movimientos = movimientosParaMostrar,
            onEditarMovimiento = onEditarMovimiento,
            onEliminarMovimiento = onEliminarMovimiento,
            isInitialLoading = uiState.isInitialLoading
        )
    }
}
