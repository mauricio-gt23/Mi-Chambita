package com.michambita.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Movimiento
import com.michambita.domain.usecase.CalcularResumenUseCase
import com.michambita.domain.usecase.GetMovimientosOnlineUseCase
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
    private val getMovimientosOnlineUseCase: GetMovimientosOnlineUseCase,
    private val calcularResumenUseCase: CalcularResumenUseCase
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    // ── Online-first ──
    private val _movimientos = MutableStateFlow<List<Movimiento>>(emptyList())
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    init {
        // Online-first: Firestore
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
        val resumen = calcularResumenUseCase(movimientos)

        _homeUiState.update { currentState ->
            currentState.copy(
                ventas = "S/ ${resumen.totalIngresos.toPlainString()}",
                gastos = "S/ ${resumen.totalGastos.toPlainString()}",
                total = "S/ ${resumen.balance.abs().toPlainString()}",
                isTotalPositive = resumen.isBalancePositive
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
