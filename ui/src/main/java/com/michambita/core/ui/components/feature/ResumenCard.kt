package com.michambita.ui.components.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ResumenCard(
        title: String,
        ventas: String,
        gastos: String,
        total: String,
        isTotalPositive: Boolean,
        isInitialLoading: Boolean,
        modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
        )

        Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResumenRow(
                        icon = Icons.AutoMirrored.Rounded.TrendingUp,
                        title = "INGRESOS",
                        amount = ventas,
                        amountColor = MaterialTheme.colorScheme.primary,
                        isInitialLoading = isInitialLoading
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                ResumenRow(
                        icon = Icons.AutoMirrored.Rounded.TrendingDown,
                        title = "GASTOS",
                        amount = gastos,
                        amountColor = MaterialTheme.colorScheme.error,
                        isInitialLoading = isInitialLoading
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                ResumenRow(
                        icon = Icons.Rounded.AccountBalance,
                        title = "BALANCE",
                        amount = total,
                        amountColor = if (isTotalPositive) MaterialTheme.colorScheme.tertiary
                                else MaterialTheme.colorScheme.error,
                        isInitialLoading = isInitialLoading,
                        emphasized = true
                )
            }
        }
    }
}

@Composable
internal fun ResumenRow(
        icon: ImageVector,
        title: String,
        amount: String,
        amountColor: Color,
        isInitialLoading: Boolean,
        emphasized: Boolean = false
) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = amountColor,
                    modifier = Modifier.size(if (emphasized) 20.dp else 18.dp)
            )
            Text(
                    text = title,
                    style = if (emphasized) MaterialTheme.typography.titleSmall
                            else MaterialTheme.typography.bodyLarge,
                    fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (isInitialLoading) {
            CircularProgressIndicator(
                    color = amountColor,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                    text = amount,
                    style = if (emphasized) MaterialTheme.typography.titleLarge
                            else MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = amountColor
            )
        }
    }
}
