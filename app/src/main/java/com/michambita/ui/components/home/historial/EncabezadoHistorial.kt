package com.michambita.ui.components.home.historial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Movimiento

@Composable
fun EncabezadoHistorial(
    modifier: Modifier = Modifier,
    movimientosPendientes: List<Movimiento>,
    onSincronizarMovimiento: () -> Unit
) {
    val isValid = movimientosPendientes.isNotEmpty()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Movimientos por sincronizar (${movimientosPendientes.size})",
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(onClick = onSincronizarMovimiento, enabled = isValid) {
            Icon(Icons.Default.Sync, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Sincronizar")
        }
    }
}