package com.michambita.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
 

@Composable
fun ResumenDiario(
    ventas: String,
    gastos: String,
    movimientosPendientesAyer: Int = 0,
    isInitialLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryTile(
                title = "Ventas de Hoy",
                icon = Icons.Filled.AttachMoney,
                amount = ventas,
                color = MaterialTheme.colorScheme.primary,
                isInitialLoading = isInitialLoading,
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                title = "Gastos de Hoy",
                icon = Icons.Filled.MoneyOff,
                amount = gastos,
                color = MaterialTheme.colorScheme.error,
                isInitialLoading = isInitialLoading,
                modifier = Modifier.weight(1f)
            )
        }
        
        if (movimientosPendientesAyer > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryTile(
                    title = "Pendientes de Ayer",
                    icon = Icons.Filled.CloudQueue,
                    amount = "$movimientosPendientesAyer",
                    color = MaterialTheme.colorScheme.tertiary,
                    isInitialLoading = false,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SummaryTile(
    title: String,
    icon: ImageVector,
    amount: String,
    color: Color,
    isInitialLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val spinnerVisible = isInitialLoading

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(36.dp))
            Text(title, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))
            if (spinnerVisible) {
                CircularProgressIndicator(
                    color = color,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    amount,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
