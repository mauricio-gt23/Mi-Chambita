package com.michambita.ui.components.home.historial.movimiento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
        }
    }
}