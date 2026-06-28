package com.michambita.feature.item.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.enums.EnumModoOperacion
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.MovimientoItem
import com.michambita.domain.usecase.AddMovimientoUseCase
import com.michambita.domain.usecase.AddMovimientoOnlineUseCase
import com.michambita.domain.usecase.DeleteMovimientoUseCase
import com.michambita.domain.usecase.DeleteMovimientoOnlineUseCase
import com.michambita.domain.usecase.UpdateMovimientoUseCase
import com.michambita.domain.usecase.UpdateMovimientoOnlineUseCase
import com.michambita.common.UiState
import com.michambita.ui.components.widget.SnackbarEvent
import com.michambita.ui.components.widget.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class MovimientoUiState(
        val modoOperacion: EnumModoOperacion = EnumModoOperacion.REGISTRAR,
        val movimientoRegEdit: Movimiento? = null,
        val tipoMovimiento: EnumTipoMovimiento = EnumTipoMovimiento.INCOME
)

@HiltViewModel
class MovimientoViewModel @Inject constructor(
    private val addMovimientoUseCase: AddMovimientoUseCase,
    private val updateMovimientoUseCase: UpdateMovimientoUseCase,
    private val deleteMovimientoUseCase: DeleteMovimientoUseCase,
    private val addMovimientoOnlineUseCase: AddMovimientoOnlineUseCase,
    private val updateMovimientoOnlineUseCase: UpdateMovimientoOnlineUseCase,
    private val deleteMovimientoOnlineUseCase: DeleteMovimientoOnlineUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovimientoUiState())
    val uiState: StateFlow<MovimientoUiState> = _uiState.asStateFlow()

    // Estado de operación CRUD online (Loading/Success/Error)
    private val _operationState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val operationState: StateFlow<UiState<String>> = _operationState.asStateFlow()

    // Canal de eventos de Snackbar
    private val _snackbarChannel = Channel<SnackbarEvent>(Channel.BUFFERED)
    val snackbarEvent = _snackbarChannel.receiveAsFlow()

    fun onRegistrarVenta() {
        _uiState.update {
            it.copy(
                tipoMovimiento = EnumTipoMovimiento.INCOME,
                modoOperacion = EnumModoOperacion.REGISTRAR,
                movimientoRegEdit = Movimiento(
                    descripcion = "",
                    monto = BigDecimal.ZERO,
                    tipoMovimiento = EnumTipoMovimiento.INCOME,
                    esMovimientoRapido = true
                )
            )
        }
    }

    fun onRegistrarGasto() {
        _uiState.update {
            it.copy(
                tipoMovimiento = EnumTipoMovimiento.EXPENSE,
                modoOperacion = EnumModoOperacion.REGISTRAR,
                movimientoRegEdit = Movimiento(
                    descripcion = "",
                    monto = BigDecimal.ZERO,
                    tipoMovimiento = EnumTipoMovimiento.EXPENSE,
                    esMovimientoRapido = true
                )
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
            viewModelScope.launch {
                _operationState.value = UiState.Loading

                val result = when (currentState.modoOperacion) {
                    EnumModoOperacion.REGISTRAR -> addMovimientoOnlineUseCase(movimiento)
                    EnumModoOperacion.EDITAR -> updateMovimientoOnlineUseCase(movimiento)
                }

                result.fold(
                    onSuccess = {
                        val message = when (currentState.modoOperacion) {
                            EnumModoOperacion.REGISTRAR -> "Movimiento registrado"
                            EnumModoOperacion.EDITAR -> "Movimiento actualizado"
                        }
                        _operationState.value = UiState.Success(message)
                        _snackbarChannel.send(SnackbarEvent(message, SnackbarType.Success))
                    },
                    onFailure = {
                        val errorMsg = it.message ?: "Error al guardar"
                        _operationState.value = UiState.Error(errorMsg)
                        _snackbarChannel.send(SnackbarEvent(errorMsg, SnackbarType.Error))
                    }
                )

                _uiState.update {
                    it.copy(
                        modoOperacion = EnumModoOperacion.REGISTRAR,
                        movimientoRegEdit = null,
                    )
                }
            }
        }
    }

    fun onMovimientoChange(movimiento: Movimiento) {
        _uiState.update { it.copy(movimientoRegEdit = movimiento) }
    }

    fun setItemsMovimiento(items: List<MovimientoItem>) {
        _uiState.update { state ->
            state.copy(movimientoRegEdit = state.movimientoRegEdit?.copy(items = items))
        }
    }

    fun deleteMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            _operationState.value = UiState.Loading

            deleteMovimientoOnlineUseCase(movimiento).fold(
                onSuccess = {
                    _operationState.value = UiState.Success("Movimiento eliminado")
                    _snackbarChannel.send(SnackbarEvent("Movimiento eliminado", SnackbarType.Success))
                },
                onFailure = {
                    val errorMsg = it.message ?: "Error al eliminar"
                    _operationState.value = UiState.Error(errorMsg)
                    _snackbarChannel.send(SnackbarEvent(errorMsg, SnackbarType.Error))
                }
            )
        }
    }

    fun clearOperationState() {
        _operationState.value = UiState.Empty
    }

    // ── Offline-first methods (preserved for future use) ──────────────

    fun addMovimiento(movimiento: Movimiento) {
        viewModelScope.launch { addMovimientoUseCase(movimiento) }
    }

    fun updateMovimiento(movimiento: Movimiento) {
        viewModelScope.launch { updateMovimientoUseCase(movimiento) }
    }

    fun deleteMovimientoOffline(movimiento: Movimiento) {
        viewModelScope.launch { deleteMovimientoUseCase(movimiento) }
    }
}