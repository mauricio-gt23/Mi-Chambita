package com.michambita.ui.components.home.historial.movimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeMovimientoItem(
    movimiento: Movimiento,
    onEditar: (Movimiento) -> Unit,
    onEliminar: (Movimiento) -> Unit,
) {
    // Si está sincronizado, solo mostrar sin swipe
    if (movimiento.sincronizado) {
        MovimientoItem(movimiento)
        return
    }
    
    // Si NO está sincronizado, permitir swipe
    val scope = rememberCoroutineScope()
    var pendingReset by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onEliminar(movimiento)
                    pendingReset = true
                    false
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEditar(movimiento)
                    pendingReset = true
                    false
                }
                else -> true
            }
        }
    )

    LaunchedEffect(pendingReset) {
        if (pendingReset) {
            scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
            pendingReset = false
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val color = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Color(0xFF4CAF50)
                SwipeToDismissBoxValue.EndToStart -> Color(0xFFF44336)
                else -> Color.Transparent
            }
            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                else -> null
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                    else -> Alignment.Center
                }
            ) {
                icon?.let {
                    Icon(it, contentDescription = null, tint = Color.White)
                }
            }
        }
    ) {
        MovimientoItem(movimiento)
    }
}
