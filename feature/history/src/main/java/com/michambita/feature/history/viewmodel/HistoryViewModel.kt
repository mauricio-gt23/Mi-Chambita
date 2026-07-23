package com.michambita.feature.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.model.Item
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.MovimientoItem
import com.michambita.domain.usecase.CalcularResumenUseCase
import com.michambita.domain.usecase.DeleteMovimientoOnlineUseCase
import com.michambita.domain.usecase.GetMovimientosHistorialUseCase
import com.michambita.domain.usecase.LoadAllItemsByCompanyIdUseCase
import com.michambita.domain.usecase.UpdateMovimientoOnlineUseCase
import com.michambita.core.common.util.DateUtils
import com.michambita.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import java.math.BigDecimal

// ── Date filter sealed class ────────────────────────────────────────────

sealed class DateFilter(val label: String) {
    object Today : DateFilter("Hoy")
    object ThisWeek : DateFilter("Semana")
    object ThisMonth : DateFilter("Mes")
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
    val typeFilter: EnumTipoMovimiento? = null,
    val isLoading: Boolean = false,
    val totalVentas: String = "S/ 0.00",
    val totalGastos: String = "S/ 0.00",
    val balance: String = "S/ 0.00",
    val isBalancePositive: Boolean = true,
    val showDatePicker: Boolean = false,
    val operationState: UiState<String> = UiState.Empty,
    // ── Edición vía MovimientoSheet ──────────────────────────────────
    val sheetVisible: Boolean = false,
    val items: List<Item> = emptyList(),
    val movimientoEnEdicion: Movimiento? = null,
    val originalItems: List<MovimientoItem> = emptyList()
)

// ── ViewModel ───────────────────────────────────────────────────────────

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMovimientosHistorialUseCase: GetMovimientosHistorialUseCase,
    private val deleteMovimientoOnlineUseCase: DeleteMovimientoOnlineUseCase,
    private val updateMovimientoOnlineUseCase: UpdateMovimientoOnlineUseCase,
    private val loadAllItemsByCompanyIdUseCase: LoadAllItemsByCompanyIdUseCase,
    private val calcularResumenUseCase: CalcularResumenUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    private var allMovimientos: MutableList<Movimiento> = mutableListOf()

    companion object {
        private val dayFormat = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es"))
    }

    init {
        loadMovimientos()
        loadItems()
    }

    // ── Public actions ──────────────────────────────────────────────────

    fun onDateFilterChanged(filter: DateFilter) {
        if (filter is DateFilter.Custom) {
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

    // ── Edición vía MovimientoSheet ─────────────────────────────────────
    private fun loadItems() {
        viewModelScope.launch {
            loadAllItemsByCompanyIdUseCase.invoke()
                .onSuccess { items -> _uiState.update { it.copy(items = items) } }
        }
    }

    fun onEditarMovimiento(movimiento: Movimiento) {
        _uiState.update {
            it.copy(
                movimientoEnEdicion = movimiento,
                originalItems = movimiento.items,
                sheetVisible = true
            )
        }
    }

    fun onMovimientoChange(movimiento: Movimiento) {
        _uiState.update { it.copy(movimientoEnEdicion = movimiento) }
    }

    fun onGuardarMovimiento() {
        val currentState = _uiState.value
        val movimiento = currentState.movimientoEnEdicion ?: return

        if (movimiento.descripcion.isBlank() || movimiento.monto <= BigDecimal.ZERO) return

        viewModelScope.launch {
            _uiState.update { it.copy(operationState = UiState.Loading) }

            updateMovimientoOnlineUseCase(movimiento, currentState.originalItems).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            sheetVisible = false,
                            movimientoEnEdicion = null,
                            originalItems = emptyList(),
                            operationState = UiState.Success("Movimiento actualizado")
                        )
                    }
                    resetAndLoad()
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            operationState = UiState.Error(throwable.message ?: "Error al guardar")
                        )
                    }
                }
            )
        }
    }

    fun dismissSheet() {
        _uiState.update {
            it.copy(
                sheetVisible = false,
                movimientoEnEdicion = null,
                originalItems = emptyList()
            )
        }
    }

    fun deleteMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            val result = deleteMovimientoOnlineUseCase(movimiento)
            result.fold(
                onSuccess = {
                    allMovimientos.removeAll { it.id == movimiento.id }
                    applyTypeFilterAndUpdateUI()
                    calcularResumen(allMovimientos)
                    _uiState.update { it.copy(operationState = UiState.Success("Movimiento eliminado")) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(operationState = UiState.Error(e.message ?: "Error al eliminar")) }
                }
            )
        }
    }

    fun clearOperationState() {
        _uiState.update { it.copy(operationState = UiState.Empty) }
    }

    // ── Internal helpers ────────────────────────────────────────────────

    private fun resetAndLoad() {
        allMovimientos.clear()
        _uiState.update {
            it.copy(groupedMovimientos = emptyMap())
        }
        loadMovimientos()
    }

    private fun loadMovimientos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val (fechaInicio, fechaFin) = _uiState.value.dateFilter.toDateRange()

            val result = getMovimientosHistorialUseCase(
                fechaInicio = fechaInicio,
                fechaFin = fechaFin
            )

            result.fold(
                onSuccess = { movimientos ->
                    allMovimientos = movimientos.toMutableList()
                    _uiState.update { it.copy(isLoading = false) }
                    applyTypeFilterAndUpdateUI()
                    calcularResumen(allMovimientos)
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            operationState = UiState.Error(e.message ?: "Error al cargar historial")
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

        val grouped = filtered
            .sortedByDescending { it.fechaRegistro }
            .groupBy { movimiento ->
                if (DateUtils.isToday(movimiento.fechaRegistro)) {
                    "Hoy"
                } else {
                    dayFormat.format(movimiento.fechaRegistro)
                        .replaceFirstChar { it.uppercase() }
                }
            }

        _uiState.update { it.copy(groupedMovimientos = grouped) }
    }

    private fun calcularResumen(movimientos: List<Movimiento>) {
        val resumen = calcularResumenUseCase(movimientos)

        _uiState.update {
            it.copy(
                totalVentas = "S/ ${resumen.totalIngresos.toPlainString()}",
                totalGastos = "S/ ${resumen.totalGastos.toPlainString()}",
                balance = "S/ ${resumen.balance.abs().toPlainString()}",
                isBalancePositive = resumen.isBalancePositive
            )
        }
    }
}
