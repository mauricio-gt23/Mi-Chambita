package com.michambita.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michambita.domain.enums.EnumModoOperacion
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.MovimientoItem
import com.michambita.domain.model.Item
import com.michambita.domain.usecase.AddMovimientoUseCase
import com.michambita.domain.usecase.AddMovimientoOnlineUseCase
import com.michambita.domain.usecase.DeleteMovimientoUseCase
import com.michambita.domain.usecase.DeleteMovimientoOnlineUseCase
import com.michambita.domain.usecase.UpdateMovimientoUseCase
import com.michambita.domain.usecase.UpdateMovimientoOnlineUseCase
import com.michambita.domain.usecase.LoadAllItemsByCompanyIdUseCase
import com.michambita.common.UiState
import com.michambita.domain.exception.InsufficientStockException
import com.michambita.domain.exception.StockShortage
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
    val tipoMovimiento: EnumTipoMovimiento = EnumTipoMovimiento.INCOME,
    val items: List<Item> = emptyList(),
    val isLoadingItems: Boolean = false,
    val itemsError: String? = null,
    val originalItems: List<MovimientoItem> = emptyList(),
    val stockShortages: List<StockShortage> = emptyList()
)

@HiltViewModel
class MovimientoViewModel @Inject constructor(
    private val addMovimientoUseCase: AddMovimientoUseCase,
    private val updateMovimientoUseCase: UpdateMovimientoUseCase,
    private val deleteMovimientoUseCase: DeleteMovimientoUseCase,
    private val addMovimientoOnlineUseCase: AddMovimientoOnlineUseCase,
    private val updateMovimientoOnlineUseCase: UpdateMovimientoOnlineUseCase,
    private val deleteMovimientoOnlineUseCase: DeleteMovimientoOnlineUseCase,
    private val loadAllItemsByCompanyIdUseCase: LoadAllItemsByCompanyIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovimientoUiState())
    val uiState: StateFlow<MovimientoUiState> = _uiState.asStateFlow()

    // Estado de operación CRUD online (Loading/Success/Error)
    private val _operationState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val operationState: StateFlow<UiState<String>> = _operationState.asStateFlow()

    fun loadItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingItems = true, itemsError = null) }
            loadAllItemsByCompanyIdUseCase.invoke()
                .onSuccess { list ->
                    _uiState.update { it.copy(items = list, isLoadingItems = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoadingItems = false, itemsError = error.message) }
                }
        }
    }

    fun onRegistrarVenta() {
        _operationState.value = UiState.Empty
        _uiState.update {
            it.copy(
                tipoMovimiento = EnumTipoMovimiento.INCOME,
                modoOperacion = EnumModoOperacion.REGISTRAR,
                movimientoRegEdit = Movimiento(
                    descripcion = "",
                    monto = BigDecimal.ZERO,
                    tipoMovimiento = EnumTipoMovimiento.INCOME,
                    esMovimientoRapido = true
                ),
                originalItems = emptyList(),
                stockShortages = emptyList()
            )
        }
    }

    fun onRegistrarGasto() {
        _operationState.value = UiState.Empty
        _uiState.update {
            it.copy(
                tipoMovimiento = EnumTipoMovimiento.EXPENSE,
                modoOperacion = EnumModoOperacion.REGISTRAR,
                movimientoRegEdit = Movimiento(
                    descripcion = "",
                    monto = BigDecimal.ZERO,
                    tipoMovimiento = EnumTipoMovimiento.EXPENSE,
                    esMovimientoRapido = true
                ),
                originalItems = emptyList(),
                stockShortages = emptyList()
            )
        }
    }

    fun onEditarMovimiento(movimiento: Movimiento) {
        _operationState.value = UiState.Empty
        _uiState.update {
            it.copy(
                tipoMovimiento = movimiento.tipoMovimiento,
                modoOperacion = EnumModoOperacion.EDITAR,
                movimientoRegEdit = movimiento,
                originalItems = movimiento.items,
                stockShortages = emptyList()
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
                    EnumModoOperacion.EDITAR -> updateMovimientoOnlineUseCase(movimiento, currentState.originalItems)
                }

                result.fold(
                    onSuccess = {
                        val message = when (currentState.modoOperacion) {
                            EnumModoOperacion.REGISTRAR -> "Movimiento registrado"
                            EnumModoOperacion.EDITAR -> "Movimiento actualizado"
                        }
                        _operationState.value = UiState.Success(message)

                        _uiState.update {
                            it.copy(
                                modoOperacion = EnumModoOperacion.REGISTRAR,
                                movimientoRegEdit = null,
                                originalItems = emptyList(),
                                stockShortages = emptyList()
                            )
                        }
                    },
                    onFailure = {
                        val errorMsg = it.message ?: "Error al guardar"
                        if (it is InsufficientStockException) {
                            _uiState.update { state ->
                                state.copy(stockShortages = it.shortages)
                            }
                            _operationState.value = UiState.Empty
                        } else {
                            _operationState.value = UiState.Error(errorMsg)
                        }
                    }
                )
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
                },
                onFailure = {
                    val errorMsg = it.message ?: "Error al eliminar"
                    _operationState.value = UiState.Error(errorMsg)
                }
            )
        }
    }

    fun clearOperationState() {
        _operationState.value = UiState.Empty
    }

    fun clearStockShortages() {
        _uiState.update { it.copy(stockShortages = emptyList()) }
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
