package com.michambita.ui.components.home.historial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Movimiento
import com.michambita.ui.components.home.historial.movimiento.SwipeMovimientoItem
import com.michambita.utils.DateUtils

 

@Composable
fun MovimientoHistorial(
    movimientosPendientes: List<Movimiento>,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit,
    isInitialLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val spinnerVisible = isInitialLoading
    
    // Filtrar solo movimientos de hoy
    val movimientosHoy = movimientosPendientes
        .filter { DateUtils.isToday(it.fechaRegistro) }
        .sortedByDescending { it.fechaRegistro.time }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (spinnerVisible) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        strokeWidth = 4.dp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = movimientosHoy,
                        key = { it.hashCode() }
                    ) { movimiento ->
                        SwipeMovimientoItem(
                            movimiento = movimiento,
                            onEditar = onEditarMovimiento,
                            onEliminar = onEliminarMovimiento,
                        )
                    }
                }
            }
        }
    }
}
