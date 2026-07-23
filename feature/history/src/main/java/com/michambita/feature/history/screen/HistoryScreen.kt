package com.michambita.feature.history.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.core.common.util.DateUtils
import com.michambita.domain.enums.EnumModoOperacion
import com.michambita.ui.components.movimiento.MovimientoItemCard
import com.michambita.ui.components.movimiento.MovimientoSheet
import com.michambita.ui.components.movimiento.SwipeMovimientoItemCard
import com.michambita.ui.components.widget.ResumenCard
import com.michambita.domain.enums.BusinessType
import com.michambita.feature.history.components.DateFilterChips
import com.michambita.feature.history.components.DayGroupHeader
import com.michambita.feature.history.components.TypeFilterChips
import com.michambita.feature.history.viewmodel.HistoryViewModel
import com.michambita.common.UiState
import com.michambita.ui.components.widget.AlertModal
import com.michambita.ui.components.widget.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    businessType: BusinessType,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    LaunchedEffect(uiState.sheetVisible) {
        if (uiState.sheetVisible) sheetState.expand() else sheetState.hide()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ResumenCard(
            title = "RESUMEN DEL PERÍODO",
            ventas = uiState.totalVentas,
            gastos = uiState.totalGastos,
            total = uiState.balance,
            isTotalPositive = uiState.isBalancePositive,
            isInitialLoading = uiState.isLoading
        )

        DateFilterChips(
            selectedFilter = uiState.dateFilter,
            onFilterSelected = viewModel::onDateFilterChanged
        )
        TypeFilterChips(
            selectedType = uiState.typeFilter,
            onTypeSelected = viewModel::onTypeFilterChanged
        )

        // ── Lista agrupada por día ──────────────────────────────────
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    strokeWidth = 4.dp
                )
            }
        } else if (uiState.groupedMovimientos.isEmpty() && !uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay movimientos en este período",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.groupedMovimientos.forEach { (dayLabel, movimientos) ->
                    // Day header
                    item(key = "header_$dayLabel") {
                        DayGroupHeader(dayLabel = dayLabel)
                    }

                    // Movimientos for this day
                    items(
                        items = movimientos,
                        key = { it.id ?: it.hashCode().toString() }
                    ) { movimiento ->
                        val formattedDate = DateUtils.formatDate(movimiento.fechaRegistro)
                        SwipeMovimientoItemCard(
                            movimiento = movimiento,
                            formattedDate = formattedDate,
                            onEditar = viewModel::onEditarMovimiento,
                            onEliminar = viewModel::deleteMovimiento
                        )
                    }
                }
            }
        }
    }

    // ── MovimientoSheet ──────────────────────────────────
    if (uiState.sheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { viewModel.dismissSheet() },
            shape = RoundedCornerShape(
                topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp
            ),
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            MovimientoSheet(
                modifier = Modifier,
                modoOperacion = EnumModoOperacion.EDITAR,
                movimiento = uiState.movimientoEnEdicion,
                items = uiState.items,
                onMovimientoChange = viewModel::onMovimientoChange,
                onGuardarClick = { viewModel.onGuardarMovimiento() }
            )
        }
    }

    // ── DateRangePicker Dialog ───────────────────────────────────────
    if (uiState.showDatePicker) {
        val dateRangePickerState = rememberDateRangePickerState()

        DatePickerDialog(
            onDismissRequest = { viewModel.dismissDatePicker() },
            confirmButton = {
                TextButton(
                    onClick = {
                        val startMillis = dateRangePickerState.selectedStartDateMillis
                        val endMillis = dateRangePickerState.selectedEndDateMillis
                        if (startMillis != null && endMillis != null) {
                            viewModel.onCustomDateRangeSelected(startMillis, endMillis)
                        } else {
                            viewModel.dismissDatePicker()
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDatePicker() }) {
                    Text("Cancelar")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = { Text("Seleccionar rango", modifier = Modifier.padding(start = 24.dp, top = 16.dp)) },
                headline = { Text("Inicio - Fin", modifier = Modifier.padding(start = 24.dp)) },
                modifier = Modifier.height(500.dp)
            )
        }
    }

    // ── Operation ────────────────
    when (val state = uiState.operationState) {
        is UiState.Loading -> {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                LoadingOverlay(modifier = Modifier, message = "Guardando...")
            }
        }
        is UiState.Success -> {
            AlertModal(
                title = state.data,
                message = "",
                confirmButtonText = "OK",
                showDismissButton = false,
                onConfirm = { viewModel.clearOperationState() },
                onDismissRequest = { viewModel.clearOperationState() }
            )
        }
        is UiState.Error -> {
            AlertModal(
                title = state.message,
                message = "",
                confirmButtonText = "OK",
                showDismissButton = false,
                onConfirm = { viewModel.clearOperationState() },
                onDismissRequest = { viewModel.clearOperationState() }
            )
        }
        else -> {}
    }
}
