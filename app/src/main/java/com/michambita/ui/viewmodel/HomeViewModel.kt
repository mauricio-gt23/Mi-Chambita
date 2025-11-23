package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.model.Movimiento
import com.michambita.domain.usecase.GetAllMovimientoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.michambita.utils.DateUtils

data class HomeUiState(
    val ventas: String = "S/ 0.00",
    val gastos: String = "S/ 0.00",
    val bottomSheetVisible: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMovimientoUseCase: GetAllMovimientoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val movimientos: StateFlow<List<Movimiento>> =
        getAllMovimientoUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            movimientos.collect { listaMovimientos ->
                actualizarResumen(listaMovimientos)
            }
        }
    }

    private fun actualizarResumen(movimientos: List<Movimiento>) {
        val movimientosHoy = movimientos.filter { DateUtils.isToday(it.fechaRegistro) }

        val totalVentas = movimientosHoy
            .filter { it.tipoMovimiento == "V" }
            .sumOf { it.monto }

        val totalGastos = movimientosHoy
            .filter { it.tipoMovimiento == "G" }
            .sumOf { it.monto }

        _uiState.update { currentState ->
            currentState.copy(
                ventas = "S/ ${totalVentas.toPlainString()}",
                gastos = "S/ ${totalGastos.toPlainString()}"
            )
        }
    }

    fun showBottomSheet() {
        _uiState.update { it.copy(bottomSheetVisible = true) }
    }

    fun hideBottomSheet() {
        _uiState.update { it.copy(bottomSheetVisible = false) }
    }
}