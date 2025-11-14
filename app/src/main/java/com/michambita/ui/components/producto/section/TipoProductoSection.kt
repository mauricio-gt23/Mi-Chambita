package com.michambita.ui.components.producto.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.data.enum.EnumTipoProducto
import com.michambita.ui.components.producto.SectionCard

@Composable
fun TipoProductoSection(
    tipoProducto: EnumTipoProducto,
    onTipoProductoChange: (EnumTipoProducto) -> Unit,
) {
    SectionCard(title = "Tipo de producto") {
        val opciones = listOf(
            EnumTipoProducto.INVENTARIABLE to "Inventariable",
            EnumTipoProducto.NO_INVENTARIABLE to "No inventariable",
            EnumTipoProducto.SERVICIO to "Servicio"
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(opciones.size) { idx ->
                val (tp, label) = opciones[idx]
                FilterChip(
                    selected = tipoProducto == tp,
                    onClick = { onTipoProductoChange(tp) },
                    label = { Text(label) }
                )
            }
        }

        Text(
            "El tipo define si requiere stock o unidad de medida",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}