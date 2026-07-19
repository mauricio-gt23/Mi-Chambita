package com.michambita.feature.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.model.Movimiento
import com.michambita.domain.usecase.DeleteMovimientoOnlineUseCase
import com.michambita.domain.usecase.GetMovimientosHistorialUseCase
import com.michambita.core.common.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// ── Date filter sealed class ────────────────────────────────────────────

sealed class DateFilter(val label: String) {
    object Today : DateFilter("Hoy")
    object ThisWeek : DateFilter("Esta semana")
    object ThisMonth : DateFilter("Este mes")
    data class Custom(val start: Date, val end: Date) : DateFilter("Personalizado")

    fun toDateRange(): Pair<Date, Date> {
        val cal = Calendar.getInstance()
        return when (this) {
            is Today -> {
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.time
                cal.add(Calendar.DAY_OF_MONTH, 1)
                val end = cal.time
                start to end
            }
            is ThisWeek -> {
                cal.firstDayOfWeek = Calendar.MONDAY
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.time
                cal.add(Calendar.WEEK_OF_YEAR, 1)
                val end = cal.time
                start to end
            }
            is ThisMonth -> {
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.time
                cal.add(Calendar.MONTH, 1)
                val end = cal.time
                start to end
            }
            is Custom -> start to end
        }
    }
}

// ── UI State ────────────────────────────────────────────────────────────

data class HistoryUiState(
    val groupedMovimientos: Map<String, List<Movimiento>> = emptyMap(),
    val dateFilter: DateFilter = DateFilter.ThisWeek,
    val typeFilter: EnumTipoMovimiento? = null, // null = Todos
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMorePages: Boolean = true,
    val totalVentas: String = "S/ 0.00",
    val totalGastos: String = "S/ 0.00",
    val balance: String = "S/ 0.00",
    val isBalancePositive: Boolean = true,
    val showDatePicker: Boolean = false,
    val error: String? = null,
    val operationMessage: String? = null
)

// ── ViewModel ───────────────────────────────────────────────────────────

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMovimientosHistorialUseCase: GetMovimientosHistorialUseCase,
    private val deleteMovimientoOnlineUseCase: DeleteMovimientoOnlineUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    // Internal list of all loaded movimientos (before type filtering)
    private var allMovimientos: MutableList<Movimiento> = mutableListOf()
    private var lastDocumentId: String? = null

    companion object {
        private const val PAGE_SIZE = 25
        private val dayFormat = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es"))
    }

    init {
        loadMovimientos()
    }

    // ── Public actions ──────────────────────────────────────────────────

    fun onDateFilterChanged(filter: DateFilter) {
        if (filter is DateFilter.Custom) {
            // Just open the picker, don't load yet
            _uiState.update { it.copy(showDatePicker = true) }
            return
        }
        _uiState.update { it.copy(dateFilter = filter) }
        resetAndLoad()
    }

    fun onCustomDateRangeSelected(startMillis: Long, endMillis: Long) {
        val start = Date(startMillis)
        // Add 1 day to end to include the full last day
        val cal = Calendar.getInstance()
        cal.timeInMillis = endMillis
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val end = cal.time

        val filter = DateFilter.Custom(start, end)
        _uiState.update {
            it.copy(
                dateFilter = filter,
                showDatePicker = false
            )
        }
        resetAndLoad()
    }

    fun dismissDatePicker() {
        _uiState.update { it.copy(showDatePicker = false) }
    }

    fun onTypeFilterChanged(type: EnumTipoMovimiento?) {
        _uiState.update { it.copy(typeFilter = type) }
        applyTypeFilterAndUpdateUI()
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasMorePages) return
        _uiState.update { it.copy(isLoadingMore = true) }
        loadMovimientos(isNextPage = true)
    }

    fun editMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            // Will be handled by navigation to the edit sheet
        }
    }

    fun deleteMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            val result = deleteMovimientoOnlineUseCase(movimiento)
            result.fold(
                onSuccess = {
                    allMovimientos.removeAll { it.id == movimiento.id }
                    applyTypeFilterAndUpdateUI()
                    _uiState.update { it.copy(operationMessage = "Movimiento eliminado") }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message ?: "Error al eliminar") }
                }
            )
        }
    }

    fun clearOperationMessage() {
        _uiState.update { it.copy(operationMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // ── Internal helpers ────────────────────────────────────────────────

    private fun resetAndLoad() {
        allMovimientos.clear()
        lastDocumentId = null
        _uiState.update {
            it.copy(
                groupedMovimientos = emptyMap(),
                hasMorePages = true,
                error = null
            )
        }
        loadMovimientos()
    }

    private fun loadMovimientos(isNextPage: Boolean = false) {
        viewModelScope.launch {
            if (!isNextPage) {
                _uiState.update { it.copy(isLoading = true) }
            }

            val (fechaInicio, fechaFin) = _uiState.value.dateFilter.toDateRange()

            val result = getMovimientosHistorialUseCase(
                fechaInicio = fechaInicio,
                fechaFin = fechaFin,
                limit = PAGE_SIZE,
                lastDocumentId = lastDocumentId
            )

            result.fold(
                onSuccess = { movimientos ->
                    allMovimientos.addAll(movimientos)
                    lastDocumentId = movimientos.lastOrNull()?.id
                    val hasMore = movimientos.size >= PAGE_SIZE

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            hasMorePages = hasMore,
                            error = null
                        )
                    }
                    applyTypeFilterAndUpdateUI()
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = e.message ?: "Error al cargar historial"
                        )
                    }
                }
            )
        }
    }

    private fun applyTypeFilterAndUpdateUI() {
        val typeFilter = _uiState.value.typeFilter
        val filtered = if (typeFilter == null) {
            allMovimientos.toList()
        } else {
            allMovimientos.filter { it.tipoMovimiento == typeFilter }
        }

        // Calculate totals from filtered list
        val totalIncome = filtered
            .filter { it.tipoMovimiento == EnumTipoMovimiento.INCOME }
            .sumOf { it.monto }
        val totalExpense = filtered
            .filter { it.tipoMovimiento == EnumTipoMovimiento.EXPENSE }
            .sumOf { it.monto }
        val rawBalance = totalIncome.subtract(totalExpense)
        val isPositive = rawBalance >= BigDecimal.ZERO

        // Group by day
        val grouped = filtered
            .sortedByDescending { it.fechaRegistro }
            .groupBy { movimiento ->
                val cal = Calendar.getInstance()
                cal.time = movimiento.fechaRegistro
                if (DateUtils.isToday(movimiento.fechaRegistro)) {
                    "Hoy"
                } else {
                    dayFormat.format(movimiento.fechaRegistro)
                        .replaceFirstChar { it.uppercase() }
                }
            }

        _uiState.update {
            it.copy(
                groupedMovimientos = grouped,
                totalVentas = "S/ ${totalIncome.toPlainString()}",
                totalGastos = "S/ ${totalExpense.toPlainString()}",
                balance = "S/ ${rawBalance.abs().toPlainString()}",
                isBalancePositive = isPositive
            )
        }
    }

    /**
     * Determines if a movimiento is within the current week (Monday to Sunday)
     * and thus eligible for edit/delete actions.
     */
    fun isMovimientoEditable(movimiento: Movimiento): Boolean {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return movimiento.fechaRegistro >= cal.time
    }
}
