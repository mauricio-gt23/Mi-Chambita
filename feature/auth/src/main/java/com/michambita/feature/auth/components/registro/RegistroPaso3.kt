package com.michambita.feature.auth.components.registro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michambita.domain.enums.BusinessType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroPaso3(
    selectedBusinessType: BusinessType?,
    onBusinessTypeSelected: (BusinessType) -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    val isValid = selectedBusinessType != null

    Text(
        text = "¿Cómo funciona tu negocio?",
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Esto nos ayuda a adaptar la app a tus necesidades",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(24.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BusinessTypeCard(
            icon = Icons.Default.Inventory2,
            title = "Vendo productos",
            description = "Administra tu inventario y registra ventas",
            isSelected = selectedBusinessType == BusinessType.INVENTORY,
            onClick = { onBusinessTypeSelected(BusinessType.INVENTORY) }
        )

        BusinessTypeCard(
            icon = Icons.Default.MiscellaneousServices,
            title = "Ofrezco servicios",
            description = "Gestiona tus servicios y citas",
            isSelected = selectedBusinessType == BusinessType.SERVICE,
            onClick = { onBusinessTypeSelected(BusinessType.SERVICE) }
        )

        BusinessTypeCard(
            icon = Icons.Default.MenuBook,
            title = "Solo registro ingresos/gastos",
            description = "Registra tus ingresos y gastos de forma simple",
            isSelected = selectedBusinessType == BusinessType.CASH_FLOW,
            onClick = { onBusinessTypeSelected(BusinessType.CASH_FLOW) }
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(1f)
        ) {
            Text("Atrás")
        }

        Button(
            onClick = onSubmit,
            enabled = isValid,
            modifier = Modifier.weight(1f)
        ) {
            Text("Registrarse")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BusinessTypeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
