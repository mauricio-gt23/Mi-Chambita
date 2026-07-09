package com.michambita.ui.components.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Tipos de Snackbar disponibles con colores del tema Material 3.
 */
sealed class SnackbarType {
    data object Success : SnackbarType()
    data object Error : SnackbarType()
    data object Info : SnackbarType()
}

/**
 * Evento de Snackbar que contiene el mensaje y el tipo.
 */
data class SnackbarEvent(
    val message: String,
    val type: SnackbarType = SnackbarType.Info
)

/**
 * Host de Snackbar genérico con colores dinámicos según el tipo de evento.
 *
 * @param snackbarHostState Estado del SnackbarHost de Material 3.
 * @param currentEvent Evento actual para determinar los colores del Snackbar.
 * @param modifier Modificador opcional.
 */
@Composable
fun SnackbarHost(
    snackbarHostState: SnackbarHostState,
    currentEvent: SnackbarEvent?,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            val containerColor = when (currentEvent?.type) {
                is SnackbarType.Success -> MaterialTheme.colorScheme.primaryContainer
                is SnackbarType.Error -> MaterialTheme.colorScheme.errorContainer
                is SnackbarType.Info -> MaterialTheme.colorScheme.secondaryContainer
                null -> MaterialTheme.colorScheme.secondaryContainer
            }
            val contentColor = when (currentEvent?.type) {
                is SnackbarType.Success -> MaterialTheme.colorScheme.onPrimaryContainer
                is SnackbarType.Error -> MaterialTheme.colorScheme.onErrorContainer
                is SnackbarType.Info -> MaterialTheme.colorScheme.onSecondaryContainer
                null -> MaterialTheme.colorScheme.onSecondaryContainer
            }

            Snackbar(
                modifier = Modifier.padding(12.dp),
                containerColor = containerColor,
                contentColor = contentColor,
            ) {
                Text(
                    text = snackbarData.visuals.message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}
