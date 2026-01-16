package com.michambita.ui.components.home.historial

import androidx.compose.foundation.layout.Arrangement
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
    movimientos: List<Movimiento>,
    onSincronizarMovimiento: () -> Unit
) {
    val pendientes = movimientos.filter { !it.sincronizado }
    val isValid = pendientes.isNotEmpty()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "MOVIMIENTOS (${movimientos.size})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* TODO: Ver historial completo */ },
                enabled = true
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = "Ver historial",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            IconButton(
                onClick = onSincronizarMovimiento,
                enabled = isValid
            ) {
                Icon(
                    Icons.Default.Cloud,
                    contentDescription = "Sincronizar",
                    tint = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}