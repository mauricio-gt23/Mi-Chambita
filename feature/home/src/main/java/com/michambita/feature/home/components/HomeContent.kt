package com.michambita.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Movimiento
import com.michambita.feature.home.config.HomeUiConfig
import com.michambita.feature.home.components.historial.EncabezadoHistorial
import com.michambita.feature.home.components.historial.MovimientoHistorial
import com.michambita.feature.home.viewmodel.HomeUiState
import com.michambita.ui.components.widget.ResumenCard

@Composable
fun HomeContent(
    uiState: HomeUiState,
    uiConfig: HomeUiConfig,
    onProductosClick: () -> Unit,
    onInventarioClick: () -> Unit,
    modifier: Modifier = Modifier,
    movimientos: List<Movimiento>,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit,
    onHistorialClick: () -> Unit = {}
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
        ResumenCard(
            title = "RESUMEN DEL DÍA",
            ventas = uiState.ventas,
            gastos = uiState.gastos,
            total = uiState.total,
            isTotalPositive = uiState.isTotalPositive,
            isInitialLoading = uiState.isInitialLoading,
            modifier = modifier
        )

        HomeAcciones(
            uiConfig = uiConfig,
            onRegistrarVenta = onRegistrarVenta,
            onRegistrarGasto = onRegistrarGasto,
            onProductosClick = onProductosClick,
            onInventarioClick = onInventarioClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        EncabezadoHistorial(modifier, onVerHistorial = onHistorialClick)
        MovimientoHistorial(
            movimientos = movimientosParaMostrar,
            onEditarMovimiento = onEditarMovimiento,
            onEliminarMovimiento = onEliminarMovimiento,
            isInitialLoading = uiState.isInitialLoading
        )
    }
}

