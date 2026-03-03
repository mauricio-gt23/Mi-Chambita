package com.michambita.feature.home.components.historial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.core.domain.model.Movimiento
import com.michambita.feature.home.components.historial.movimiento.SwipeMovimientoItem


@Composable
fun MovimientoHistorial(
    movimientos: List<Movimiento>,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit,
    isInitialLoading: Boolean,
    modifier: Modifier = Modifier
) {
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
            if (isInitialLoading) {
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
                        items = movimientos,
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
