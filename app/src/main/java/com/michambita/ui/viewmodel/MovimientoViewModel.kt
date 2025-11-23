package com.michambita.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.data.enums.EnumModoOperacion
import com.michambita.data.enums.EnumTipoMovimiento
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.MovimientoItem
import com.michambita.domain.usecase.AddMovimientoUseCase
import com.michambita.domain.usecase.DeleteMovimientoUseCase
import com.michambita.domain.usecase.UpdateMovimientoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class MovimientoUiState(
        val modoOperacion: EnumModoOperacion = EnumModoOperacion.REGISTRAR,
        val movimientoRegEdit: Movimiento? = null,
        val tipoMovimiento: EnumTipoMovimiento = EnumTipoMovimiento.VENTA,
        val ventaRapida: Boolean = true
)

@HiltViewModel
class MovimientoViewModel @Inject constructor(
    private val addMovimientoUseCase: AddMovimientoUseCase,
    private val updateMovimientoUseCase: UpdateMovimientoUseCase,
    private val deleteMovimientoUseCase: DeleteMovimientoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovimientoUiState())
    val uiState: StateFlow<MovimientoUiState> = _uiState.asStateFlow()

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
                tipoMovimiento = EnumTipoMovimiento.VENTA,
                modoOperacion = EnumModoOperacion.REGISTRAR,
                movimientoRegEdit = Movimiento(
                    descripcion = "",
                    monto = BigDecimal.ZERO,
                    tipoMovimiento = EnumTipoMovimiento.VENTA
                ),
                ventaRapida = true
            )
        }
    }

    fun onRegistrarGasto() {
        _uiState.update {
            it.copy(
                tipoMovimiento = EnumTipoMovimiento.GASTO,
                modoOperacion = EnumModoOperacion.REGISTRAR,
                movimientoRegEdit = Movimiento(
                    descripcion = "",
                    monto = BigDecimal.ZERO,
                    tipoMovimiento = EnumTipoMovimiento.GASTO
                ),
                ventaRapida = true
            )
        }
    }

    fun onEditarMovimiento(movimiento: Movimiento) {
        _uiState.update {
            it.copy(
                tipoMovimiento = movimiento.tipoMovimiento,
                modoOperacion = EnumModoOperacion.EDITAR,
                movimientoRegEdit = movimiento
            )
        }
    }

    fun onGuardarMovimiento() {
        val currentState = _uiState.value
        val movimiento = currentState.movimientoRegEdit

        if (movimiento != null && movimiento.descripcion.isNotBlank() && movimiento.monto > BigDecimal.ZERO) {
            val movimientoConItems = movimiento
            when (currentState.modoOperacion) {
                EnumModoOperacion.REGISTRAR -> {
                    addMovimiento(movimientoConItems)
                }
                EnumModoOperacion.EDITAR -> {
                    updateMovimiento(movimientoConItems)
                }
            }

            _uiState.update {
                it.copy(
                    modoOperacion = EnumModoOperacion.REGISTRAR,
                    movimientoRegEdit = null,
                )
            }
        }
    }

    fun setVentaRapida(value: Boolean) {
        _uiState.update { it.copy(ventaRapida = value) }
    }

    fun setItemsMovimiento(items: List<MovimientoItem>) {
        _uiState.update { state ->
            state.copy(movimientoRegEdit = state.movimientoRegEdit?.copy(items = items))
        }
    }

    fun addMovimiento(movimiento: Movimiento) {
        viewModelScope.launch { addMovimientoUseCase(movimiento) }
    }

    fun updateMovimiento(movimiento: Movimiento) {
        viewModelScope.launch { updateMovimientoUseCase(movimiento) }
    }

    fun deleteMovimiento(movimiento: Movimiento) {
        viewModelScope.launch { deleteMovimientoUseCase(movimiento) }
    }
}