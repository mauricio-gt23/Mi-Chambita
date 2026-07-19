package com.michambita.feature.history.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.core.common.util.DateUtils
import com.michambita.ui.components.movimiento.MovimientoItemCard
import com.michambita.ui.components.movimiento.SwipeMovimientoItemCard
import com.michambita.ui.components.widget.ResumenCard
import com.michambita.domain.enums.BusinessType
import com.michambita.feature.history.components.DateFilterChips
import com.michambita.feature.history.components.DayGroupHeader
import com.michambita.feature.history.components.TypeFilterChips
import com.michambita.feature.history.viewmodel.HistoryViewModel
import com.michambita.ui.components.widget.AlertModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    businessType: BusinessType,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Detect when user scrolls near the end for pagination
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 3 && totalItems > 0
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !uiState.isLoading && !uiState.isLoadingMore && uiState.hasMorePages) {
            viewModel.loadNextPage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── Resumen del período ──────────────────────────────────────
        ResumenCard(
            title = "RESUMEN DEL PERÍODO",
            ventas = uiState.totalVentas,
            gastos = uiState.totalGastos,
            total = uiState.balance,
            isTotalPositive = uiState.isBalancePositive,
            isInitialLoading = uiState.isLoading && uiState.groupedMovimientos.isEmpty()
        )

        // ── Filtros ─────────────────────────────────────────────────
        DateFilterChips(
            selectedFilter = uiState.dateFilter,
            onFilterSelected = viewModel::onDateFilterChanged
        )

        TypeFilterChips(
            selectedType = uiState.typeFilter,
            onTypeSelected = viewModel::onTypeFilterChanged
        )

        // ── Lista agrupada por día ──────────────────────────────────
        if (uiState.isLoading && uiState.groupedMovimientos.isEmpty()) {
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

                        if (viewModel.isMovimientoEditable(movimiento)) {
                            SwipeMovimientoItemCard(
                                movimiento = movimiento,
                                formattedDate = formattedDate,
                                onEditar = { /* TODO: navigate to edit */ },
                                onEliminar = viewModel::deleteMovimiento
                            )
                        } else {
                            MovimientoItemCard(
                                movimiento = movimiento,
                                formattedDate = formattedDate
                            )
                        }
                    }
                }

                // Loading more indicator
                if (uiState.isLoadingMore) {
                    item(key = "loading_more") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }
            }
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

    // ── Operation feedback ──────────────────────────────────────────
    uiState.operationMessage?.let { message ->
        AlertModal(
            title = message,
            message = "",
            confirmButtonText = "OK",
            showDismissButton = false,
            onConfirm = { viewModel.clearOperationMessage() },
            onDismissRequest = { viewModel.clearOperationMessage() }
        )
    }

    uiState.error?.let { errorMsg ->
        AlertModal(
            title = "Error",
            message = errorMsg,
            confirmButtonText = "OK",
            showDismissButton = false,
            onConfirm = { viewModel.clearError() },
            onDismissRequest = { viewModel.clearError() }
        )
    }
}
