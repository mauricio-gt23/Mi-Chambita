package com.michambita.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeAcciones(
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit,
    onProductosClick: () -> Unit,
    onInventarioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                "Registrar Venta",
                Icons.Filled.AddShoppingCart,
                onRegistrarVenta,
                Modifier.weight(1f)
            )
            ActionButton(
                "Registrar Gasto",
                Icons.Filled.Payment,
                onRegistrarGasto,
                Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                "Productos", 
                Icons.Filled.AddBox, 
                onClick = onProductosClick, 
                isSecondary = true,
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                "Inventario",
                Icons.Filled.Inventory, 
                onClick = onInventarioClick, 
                isSecondary = true,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                "Estadisticas",
                Icons.Filled.AttachMoney,
                onClick = onProductosClick,
                isSecondary = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false
) {
    val colors =
        if (isSecondary) ButtonDefaults.outlinedButtonColors() else ButtonDefaults.filledTonalButtonColors()
    val border = if (isSecondary) ButtonDefaults.outlinedButtonBorder else null

    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = colors,
        border = border,
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 16.sp)
    }
}