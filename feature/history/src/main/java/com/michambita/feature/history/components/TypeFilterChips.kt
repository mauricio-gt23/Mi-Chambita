package com.michambita.feature.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michambita.domain.enums.EnumTipoMovimiento

@Composable
fun TypeFilterChips(
    selectedType: EnumTipoMovimiento?,
    onTypeSelected: (EnumTipoMovimiento?) -> Unit,
    modifier: Modifier = Modifier
) {
    data class TypeOption(val label: String, val value: EnumTipoMovimiento?)

    val options = listOf(
        TypeOption("Todos", null),
        TypeOption("Ventas", EnumTipoMovimiento.INCOME),
        TypeOption("Gastos", EnumTipoMovimiento.EXPENSE)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selectedType == option.value,
                onClick = { onTypeSelected(option.value) },
                label = { Text(option.label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = when (option.value) {
                        EnumTipoMovimiento.INCOME -> MaterialTheme.colorScheme.primaryContainer
                        EnumTipoMovimiento.EXPENSE -> MaterialTheme.colorScheme.errorContainer
                        null -> MaterialTheme.colorScheme.secondaryContainer
                    },
                    selectedLabelColor = when (option.value) {
                        EnumTipoMovimiento.INCOME -> MaterialTheme.colorScheme.onPrimaryContainer
                        EnumTipoMovimiento.EXPENSE -> MaterialTheme.colorScheme.onErrorContainer
                        null -> MaterialTheme.colorScheme.onSecondaryContainer
                    }
                )
            )
        }
    }
}
