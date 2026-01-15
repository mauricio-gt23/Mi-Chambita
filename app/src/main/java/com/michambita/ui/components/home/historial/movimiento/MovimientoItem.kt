package com.michambita.ui.components.home.historial.movimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Movimiento
import com.michambita.utils.DateUtils
import com.michambita.data.enums.EnumTipoMovimiento

@Composable
fun MovimientoItem(
    movimiento: Movimiento,
) {
    val esVenta = movimiento.tipoMovimiento == EnumTipoMovimiento.VENTA
    val color = if (esVenta) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val icon = if (esVenta) Icons.Filled.PointOfSale else Icons.Filled.Receipt
    
    // Colores de estado de sincronización
    val estadoColor = if (movimiento.sincronizado) 
        MaterialTheme.colorScheme.tertiary  // Verde
    else 
        MaterialTheme.colorScheme.primary   // Naranja
        
    val estadoIcon = if (movimiento.sincronizado) 
        Icons.Filled.CloudDone 
    else 
        Icons.Filled.CloudQueue
        
    val backgroundColor = if (movimiento.sincronizado) 
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    else 
        MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra lateral de estado
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(estadoColor)
            )
            
            Spacer(Modifier.width(12.dp))
            
            Icon(icon, contentDescription = null, tint = color)
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(movimiento.descripcion, fontWeight = FontWeight.SemiBold)
                Text(DateUtils.formatDate(movimiento.fechaRegistro), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "S/ ${movimiento.monto.setScale(2)}",
                color = color,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.width(8.dp))
            
            // Ícono de estado de sincronización
            Icon(
                estadoIcon,
                contentDescription = if (movimiento.sincronizado) "Sincronizado" else "Pendiente",
                tint = estadoColor,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 12.dp)
            )
        }
    }
}