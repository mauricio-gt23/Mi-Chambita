package com.michambita.ui.components.home.historial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Movimiento
import com.michambita.ui.components.home.historial.movimiento.SwipeMovimientoItem
import com.michambita.utils.DateUtils
import kotlinx.coroutines.delay

@Composable
fun MovimientoHistorial(
    movimientosPendientes: List<Movimiento>,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit,
    modifier: Modifier = Modifier
) {
    var spinnerVisible by remember { mutableStateOf(true) }
    LaunchedEffect(movimientosPendientes) {
        if (movimientosPendientes.isEmpty()) {
            spinnerVisible = true
        } else {
            spinnerVisible = true
            delay(2000)
            spinnerVisible = false
        }
    }

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
                if (movimientosPendientes.isEmpty()) {
                    EstadoVacio()
                } else {
                    ListaMovimientos(
                        movimientos = movimientosPendientes,
                        onEditarMovimiento = onEditarMovimiento,
                        onEliminarMovimiento = onEliminarMovimiento
                    )
                }
            }
        }
    }
}

@Composable
fun EstadoVacio(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No hay movimientos pendientes.")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* lógica para ir al historial */ }) {
            Icon(Icons.Default.History, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Ver historial")
        }
    }
}

@Composable
fun ListaMovimientos(
    movimientos: List<Movimiento>,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit,
    modifier: Modifier = Modifier
) {
    val movimientosHoy = movimientos.filter { DateUtils.isToday(it.fechaRegistro) }
        .sortedByDescending { it.fechaRegistro.time }

    val movimientosAnteriores = movimientos.filter { !DateUtils.isToday(it.fechaRegistro) }
        .sortedByDescending { it.fechaRegistro.time }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (movimientosHoy.isNotEmpty()) {
            item {
                Text("Hoy", style = MaterialTheme.typography.titleSmall)
            }
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

        if (movimientosAnteriores.isNotEmpty()) {
            item {
                Text("Anteriores", style = MaterialTheme.typography.titleSmall)
            }
            items(
                items = movimientosAnteriores,
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
