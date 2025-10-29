package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.data.enum.EnumModoOperacion
import com.michambita.domain.model.Movimiento
import com.michambita.domain.usecase.AddMovimientoUseCase
import com.michambita.domain.usecase.DeleteMovimientoUseCase
import com.michambita.domain.usecase.GetAllMovimientoUseCase
import com.michambita.domain.usecase.UpdateMovimientoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class HomeUiState(
    val modoOperacion: EnumModoOperacion = EnumModoOperacion.REGISTRAR,
    val movimientoRegEdit: Movimiento? = null,
    val tipoMovimiento: String = "V", // "V" = venta, "G" = gasto
    val ventas: String = "S/ 0.00",
    val gastos: String = "S/ 0.00",
    val bottomSheetVisible: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMovimientoUseCase: GetAllMovimientoUseCase,
    private val addMovimientoUseCase: AddMovimientoUseCase,
    private val updateMovimientoUseCase: UpdateMovimientoUseCase,
    private val deleteMovimientoUseCase: DeleteMovimientoUseCase,
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
        val totalVentas = movimientos
            .filter { it.tipoMovimiento == "V" }
            .sumOf { it.monto }
        
        val totalGastos = movimientos
            .filter { it.tipoMovimiento == "G" }
            .sumOf { it.monto }
        
        _uiState.update { currentState ->
            currentState.copy(
                ventas = "S/ ${totalVentas.toPlainString()}",
                gastos = "S/ ${totalGastos.toPlainString()}"
            )
        }
    }

    fun onMovimientoChange(campo: String, valor: String) {
        _uiState.update { currentState ->
            val currentMovimiento = currentState.movimientoRegEdit ?: Movimiento(
                descripcion = "",
                monto = BigDecimal.ZERO,
                tipoMovimiento = currentState.tipoMovimiento
            )
            
            val movimientoActualizado = when (campo) {
                "titulo" -> currentMovimiento.copy(descripcion = valor)
                "monto" -> {
                    val montoDecimal = valor.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
                    currentMovimiento.copy(monto = montoDecimal)
                }
                else -> currentMovimiento
            }
            
            currentState.copy(movimientoRegEdit = movimientoActualizado)
        }
    }

    fun onRegistrarVenta() {
        _uiState.update { 
            it.copy(
                tipoMovimiento = "V",
                modoOperacion = EnumModoOperacion.REGISTRAR,
                movimientoRegEdit = Movimiento(
                    descripcion = "",
                    monto = BigDecimal.ZERO,
                    tipoMovimiento = "V"
                ),
                bottomSheetVisible = true
            ) 
        }
    }

    fun onRegistrarGasto() {
        _uiState.update { 
            it.copy(
                tipoMovimiento = "G",
                modoOperacion = EnumModoOperacion.REGISTRAR,
                movimientoRegEdit = Movimiento(
                    descripcion = "",
                    monto = BigDecimal.ZERO,
                    tipoMovimiento = "G"
                ),
                bottomSheetVisible = true
            ) 
        }
    }

    fun onEditarMovimiento(movimiento: Movimiento) {
        _uiState.update { 
            it.copy(
                tipoMovimiento = movimiento.tipoMovimiento,
                modoOperacion = EnumModoOperacion.EDITAR,
                movimientoRegEdit = movimiento,
                bottomSheetVisible = true
            ) 
        }
    }

    fun onGuardarMovimiento() {
        val currentState = _uiState.value
        val movimiento = currentState.movimientoRegEdit
        
        if (movimiento != null && movimiento.descripcion.isNotBlank() && movimiento.monto > BigDecimal.ZERO) {
            when (currentState.modoOperacion) {
                EnumModoOperacion.REGISTRAR -> {
                    addMovimiento(movimiento)
                }
                EnumModoOperacion.EDITAR -> {
                    updateMovimiento(movimiento)
                }
            }

            // Resetear valores
            _uiState.update {
                it.copy(
                    modoOperacion = EnumModoOperacion.REGISTRAR,
                    movimientoRegEdit = null,
                    bottomSheetVisible = false
                )
            }
        }
    }

    fun hideBottomSheet() {
        _uiState.update { it.copy(bottomSheetVisible = false) }
    }

    fun addMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            addMovimientoUseCase(movimiento)
        }
    }

    fun updateMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            updateMovimientoUseCase(movimiento)
        }
    }

    fun deleteMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            deleteMovimientoUseCase(movimiento)
        }
    }
}