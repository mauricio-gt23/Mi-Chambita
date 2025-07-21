package com.michambita.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Movimiento
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeMovimientoItem(
    movimiento: Movimiento,
    onEditar: (Movimiento) -> Unit,
    onEliminar: (Movimiento) -> Unit,
) {
    val dismissState = rememberDismissState()

    // Acciones
    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            DismissValue.DismissedToStart -> {
                onEliminar(movimiento)
            }
            DismissValue.DismissedToEnd -> {
                onEditar(movimiento)
            }
            else -> Unit
        }
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            val direction = dismissState.dismissDirection
            val color = when (direction) {
                DismissDirection.StartToEnd -> Color(0xFF4CAF50)
                DismissDirection.EndToStart -> Color(0xFFF44336)
                else -> Color.Transparent
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Edit
                DismissDirection.EndToStart -> Icons.Default.Delete
                else -> null
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = if (direction == DismissDirection.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                icon?.let {
                    Icon(it, contentDescription = null, tint = Color.White)
                }
            }
        },
        dismissContent = {
            MovimientoItem(movimiento)
        }
    )
}