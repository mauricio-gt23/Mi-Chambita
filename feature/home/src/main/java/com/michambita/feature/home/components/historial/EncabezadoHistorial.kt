package com.michambita.feature.home.components.historial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.michambita.domain.model.Movimiento

@Composable
fun EncabezadoHistorial(
    modifier: Modifier = Modifier,
    onVerHistorial: () -> Unit = {}
    // movimientos: List<Movimiento>,           // Offline-first: comentado para MVP online-first
    // onSincronizarMovimiento: () -> Unit       // Offline-first: comentado para MVP online-first
) {
    // Offline-first: lógica de pendientes comentada para MVP online-first
    // val pendientes = movimientos.filter { !it.sincronizado }
    // val isValid = pendientes.isNotEmpty()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "MOVIMIENTOS",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Últimos 10 movimientos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVerHistorial,
                enabled = true
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = "Ver historial",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            // Offline-first: botón de sincronización comentado para MVP online-first
            // IconButton(
            //     onClick = onSincronizarMovimiento,
            //     enabled = isValid
            // ) {
            //     Icon(
            //         Icons.Default.Cloud,
            //         contentDescription = "Sincronizar",
            //         tint = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            //         modifier = Modifier.size(28.dp)
            //     )
            // }
        }
    }
}