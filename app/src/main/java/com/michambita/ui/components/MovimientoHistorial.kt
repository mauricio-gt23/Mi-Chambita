package com.michambita.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Movimiento

@Composable
fun EncabezadoHistorial(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Movimientos por sincronizar", style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = { /* lógica de sincronización */ }) {
            Icon(Icons.Default.Sync, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Sincronizar")
        }
    }
}

@Composable
fun MovimientoHistorial(
    movimientosPendientes: List<Movimiento>,
    onEditarMovimiento: (Movimiento) -> Unit,
    onEliminarMovimiento: (Movimiento) -> Unit,
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
    LazyColumn(
        modifier = modifier.fillMaxSize(),
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