package com.michambita.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Movimiento
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.usecase.GetMovimientosOnlineUseCase
import java.math.BigDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val ventas: String = "S/ 0.00",
    val gastos: String = "S/ 0.00",
    val total: String = "S/ 0.00",
    val isTotalPositive: Boolean = true,
    val bottomSheetVisible: Boolean = false,
    val isInitialLoading: Boolean = true,
    val movimientosPendientesAyer: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovimientosOnlineUseCase: GetMovimientosOnlineUseCase
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    // ── Online-first: lectura de movimientos de hoy desde Firestore ──
    private val _movimientos = MutableStateFlow<List<Movimiento>>(emptyList())
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    init {
        // Online-first: cargar movimientos de hoy desde Firestore.
        // isInitialLoading se apaga con la primera emisión real (no con un delay fijo).
        viewModelScope.launch {
            getMovimientosOnlineUseCase().collect { listaMovimientos ->
                _movimientos.value = listaMovimientos
                actualizarResumen(listaMovimientos)
                if (_homeUiState.value.isInitialLoading) {
                    _homeUiState.update { it.copy(isInitialLoading = false) }
                }
            }
        }
    }

    private fun actualizarResumen(movimientos: List<Movimiento>) {
        // Online-first: los movimientos ya vienen filtrados por hoy desde Firestore
        val totalIncome = movimientos
            .filter { it.tipoMovimiento == EnumTipoMovimiento.INCOME }
            .sumOf { it.monto }

        val totalExpense = movimientos
            .filter { it.tipoMovimiento == EnumTipoMovimiento.EXPENSE }
            .sumOf { it.monto }

        val rawTotal = totalIncome.subtract(totalExpense)
        val isPositive = rawTotal >= BigDecimal.ZERO
        val totalAbs = rawTotal.abs()

        _homeUiState.update { currentState ->
            currentState.copy(
                ventas = "S/ ${totalIncome.toPlainString()}",
                gastos = "S/ ${totalExpense.toPlainString()}",
                total = "S/ ${totalAbs.toPlainString()}",
                isTotalPositive = isPositive
            )
        }
    }

    fun showBottomSheet() {
        _homeUiState.update { it.copy(bottomSheetVisible = true) }
    }

    fun hideBottomSheet() {
        _homeUiState.update { it.copy(bottomSheetVisible = false) }
    }
}
